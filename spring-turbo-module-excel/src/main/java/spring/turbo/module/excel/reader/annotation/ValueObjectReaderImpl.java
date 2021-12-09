/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader.annotation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Payload;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.core.SpringContext;
import spring.turbo.core.SpringContextAware;
import spring.turbo.module.excel.CellParser;
import spring.turbo.module.excel.DefaultCellParser;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.reader.HeaderConfig;
import spring.turbo.module.excel.reader.ValueObjectReadingWalkerBuilder;
import spring.turbo.util.StringFormatter;

import java.util.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class ValueObjectReaderImpl implements ValueObjectReader, SpringContextAware, InitializingBean {

    private SpringContext springContext;
    private final List<ValueObjectListener> listeners;
    private final Map<String, ConfigHolder> configMap = new HashMap<>();
    private final Map<String, ValueObjectListener<?>> listenerMap = new HashMap<>();


    public ValueObjectReaderImpl(List<ValueObjectListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void startReading(ExcelDiscriminator discriminator, Resource resource, Payload payload) {

        if (!configMap.containsKey(discriminator.getDiscriminatorValue())) {
            String msg = StringFormatter.format("cannot find configuration for {}", discriminator.getDiscriminatorValue());
            throw new IllegalArgumentException(msg);
        }

        ValueObjectListener listener = listenerMap.get(discriminator.getDiscriminatorValue());
        ConfigHolder holder = configMap.get(discriminator.getDiscriminatorValue());

        CellParser cellParser = ValueObjectUtils.newInstance(holder.cellParserType).orElse(null);
        if (cellParser == null) {
            cellParser = new DefaultCellParser();
        }


        ValueObjectReadingWalkerBuilder<?> builder = ValueObjectReadingWalkerBuilder
                .newInstance(holder.valueObjectType)
                .payload(Optional.ofNullable(payload).orElseGet(Payload::newInstance))
                .conversionService(springContext.getBean(ConversionService.class).orElseGet(DefaultFormattingConversionService::new))
                .validators(springContext.getBean(Validator.class).orElseGet(NullValidator::new))
                .onSuccess(listener::onSuccess)
                .onError(listener::onError)
                .cellParser(cellParser);

        if (!holder.includeSheetIndexes.isEmpty()) {
            builder.onlyReadSheet(holder.includeSheetIndexes.toArray(new Integer[0]));
        }

        for (Pair<Integer, Integer> pair : holder.headerConfig.getSheetIndexConfig()) {
            builder.sheetHeader(pair.getA(), pair.getB());
        }

        for (Pair<Integer, Set<Integer>> pair : holder.excludeRowSets) {
            builder.excludeRowInSet(pair.getA(), pair.getB().toArray(new Integer[0]));
        }

        for (Tuple<Integer, Integer, Integer> tuple : holder.excludeRowRanges) {
            builder.excludeRowInRange(tuple.getA(), tuple.getB(), tuple.getC());
        }

        builder.build(holder.excelType, resource)
                .walk();
    }

    @Override
    public void afterPropertiesSet() {
        for (ValueObjectListener<?> listener : listeners) {
            ValueObjectReading annotation = AnnotationUtils.findAnnotation(listener.getClass(), ValueObjectReading.class);
            if (annotation == null) {
                continue;
            }

            ConfigHolder holder = new ConfigHolder();
            holder.discriminatorValue = annotation.discriminatorValue();
            holder.excelType = annotation.excelType();
            holder.valueObjectType = annotation.valueObjectType();
            holder.cellParserType = annotation.cellParser();

            this.setIncludeSheetIndexes(holder, annotation);
            this.setHeader(holder, annotation);
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

    private static class ConfigHolder {
        private String discriminatorValue;
        private ExcelType excelType;
        private final Set<Integer> includeSheetIndexes = new HashSet<>();
        private final HeaderConfig headerConfig = HeaderConfig.newInstance();
        private final Set<Pair<Integer, Set<Integer>>> excludeRowSets = new HashSet<>();
        private final Set<Tuple<Integer, Integer, Integer>> excludeRowRanges = new HashSet<>();
        private Class<?> valueObjectType;
        private Class<? extends CellParser> cellParserType;
    }

    private static class NullValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return true;
        }

        @Override
        public void validate(Object target, Errors errors) {
            // nop
        }
    }
}
