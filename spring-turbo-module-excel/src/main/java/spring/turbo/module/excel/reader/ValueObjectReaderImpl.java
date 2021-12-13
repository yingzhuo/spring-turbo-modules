/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.validation.Validator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.Alias;
import spring.turbo.bean.valueobject.NullValidator;
import spring.turbo.core.SpringContext;
import spring.turbo.core.SpringContextAware;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.config.AliasConfig;
import spring.turbo.module.excel.config.HeaderConfig;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicateFactories;
import spring.turbo.module.excel.visitor.Visitor;
import spring.turbo.util.InstanceUtils;
import spring.turbo.util.StringFormatter;

import java.util.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class ValueObjectReaderImpl implements ValueObjectReader, SpringContextAware, InitializingBean {

    private final List<Visitor> visitors;
    private final Map<String, ConfigHolder> configMap = new HashMap<>();
    private final Map<String, Visitor> listenerMap = new HashMap<>();
    private SpringContext springContext;

    ValueObjectReaderImpl(List<Visitor> visitors) {
        this.visitors = visitors;
    }

    @Override
    public void read(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload) {

        if (payload == null) {
            payload = ProcessPayload.newInstance();
        }

        if (!configMap.containsKey(discriminator.getDiscriminatorValue())) {
            String msg = StringFormatter.format("cannot find configuration for {}", discriminator.getDiscriminatorValue());
            throw new IllegalArgumentException(msg);
        }

        Visitor listener = listenerMap.get(discriminator.getDiscriminatorValue());
        ConfigHolder holder = configMap.get(discriminator.getDiscriminatorValue());

        CellParser cellParser = InstanceUtils.newInstance(holder.cellParserType).orElse(null);
        if (cellParser == null) {
            cellParser = new DefaultCellParser();
        }

        WalkerBuilder builder = Walker
                .builder(holder.valueObjectType)
                .payload(payload)
                .conversionService(springContext.getBean(ConversionService.class).orElse(null))
                .validators(getValidators(holder))
                .cellParser(cellParser)
                .headerConfig(holder.headerConfig)
                .aliasConfig(holder.aliasConfig)
                .password(holder.password)
                .visitor(listener);

        if (!holder.includeSheetIndexes.isEmpty()) {
            builder.includeSheet(SheetPredicateFactories.ofIndex(holder.includeSheetIndexes.toArray(new Integer[0])));
        }

        for (Pair<Integer, Set<Integer>> pair : holder.excludeRowSets) {
            builder.excludeRow(RowPredicateFactories.indexInSet(pair.getA(), pair.getB().toArray(new Integer[0])));
        }

        for (Tuple<Integer, Integer, Integer> tuple : holder.excludeRowRanges) {
            builder.excludeRow(RowPredicateFactories.indexInRange(tuple.getA(), tuple.getB(), tuple.getC()));
        }

        builder.build(holder.excelType, resource)
                .walk();
    }

    private List<Validator> getValidators(ConfigHolder holder) {
        final List<Validator> list = new ArrayList<>();
        springContext.getBean(Validator.class).ifPresent(list::add);    // 注意只要加入spring托管的primary-validator

        for (Class<? extends Validator> clazz : holder.additionalValidators) {
            list.add(InstanceUtils.newInstanceOrThrow(clazz));
        }

        // 实在找不到合适的拉一个垫背的
        if (list.isEmpty()) {
            list.add(NullValidator.getInstance());
        }

        return list;
    }

    @Override
    public void afterPropertiesSet() {
        for (Visitor listener : visitors) {
            // double check
            ValueObjectReading annotation = AnnotationUtils.findAnnotation(listener.getClass(), ValueObjectReading.class);
            if (annotation == null) {
                continue;
            }

            ConfigHolder holder = new ConfigHolder();
            holder.discriminatorValue = annotation.discriminatorValue();
            holder.excelType = annotation.excelType();
            holder.valueObjectType = annotation.valueObjectType();
            holder.cellParserType = annotation.cellParser();
            holder.additionalValidators = annotation.additionalValidators();
            holder.password = annotation.password();

            this.setIncludeSheetIndexes(holder, annotation);
            this.setHeader(holder, annotation);
            this.setAlias(holder, annotation);
            this.setExcludeRowSets(holder, annotation);
            this.setExcludeRowRanges(holder, annotation);

            this.configMap.put(holder.discriminatorValue, holder);
            this.listenerMap.put(holder.discriminatorValue, listener);
        }
    }

    private void setIncludeSheetIndexes(ConfigHolder holder, ValueObjectReading annotation) {
        for (int index : annotation.includeSheetIndex()) {
            holder.includeSheetIndexes.add(index);
        }
    }

    private void setHeader(ConfigHolder holder, ValueObjectReading annotation) {
        for (Header sub : annotation.headers()) {
            holder.headerConfig.bySheetIndex(sub.sheetIndex(), sub.rowIndex());
        }
    }

    private void setAlias(ConfigHolder holder, ValueObjectReading annotation) {
        for (Alias sub : annotation.aliases()) {
            holder.aliasConfig.add(sub.from(), sub.to());
        }
    }

    private void setExcludeRowSets(ConfigHolder holder, ValueObjectReading annotation) {
        for (ExcludeRowSet sub : annotation.excludeRowSets()) {
            if (sub.rowIndexes().length == 0) {
                continue;
            }

            Set<Integer> set = new HashSet<>();
            for (int i : sub.rowIndexes()) {
                set.add(i);
            }

            holder.excludeRowSets.add(Pair.of(sub.sheetIndex(), set));
        }
    }

    private void setExcludeRowRanges(ConfigHolder holder, ValueObjectReading annotation) {
        for (ExcludeRowRange sub : annotation.excludeRowRanges()) {
            Tuple<Integer, Integer, Integer> tuple = Tuple.of(sub.sheetIndex(), sub.minInclude(), sub.maxExclude());
            holder.excludeRowRanges.add(tuple);
        }
    }

    @Override
    public void setSpringContext(SpringContext springContext) {
        this.springContext = springContext;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class ConfigHolder {
        private final Set<Integer> includeSheetIndexes = new HashSet<>();
        private final HeaderConfig headerConfig = HeaderConfig.newInstance();
        private final AliasConfig aliasConfig = AliasConfig.newInstance();
        private final Set<Pair<Integer, Set<Integer>>> excludeRowSets = new HashSet<>();
        private final Set<Tuple<Integer, Integer, Integer>> excludeRowRanges = new HashSet<>();
        private String discriminatorValue;
        private ExcelType excelType;
        private Class<?> valueObjectType;
        private Class<? extends CellParser> cellParserType;
        private Class<? extends Validator>[] additionalValidators;
        private String password;
    }

}
