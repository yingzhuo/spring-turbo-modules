/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import lombok.ToString;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.validation.Validator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.NullValidator;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicateFactories;
import spring.turbo.module.excel.reader.annotation.ExcludeRowRange;
import spring.turbo.module.excel.reader.annotation.ExcludeRowSet;
import spring.turbo.module.excel.reader.annotation.Header;
import spring.turbo.module.excel.reader.annotation.*;
import spring.turbo.module.excel.visitor.BatchVisitor;
import spring.turbo.util.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
class BatchValueObjectReaderImpl implements BatchValueObjectReader, InitializingBean, ApplicationContextAware {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final List<BatchVisitor<?>> visitors;
    private final Map<String, Config> configMap = new HashMap<>();

    private ApplicationContext applicationContext;
    private ConversionService conversionService;
    private Validator[] validators;

    public BatchValueObjectReaderImpl(List<BatchVisitor<?>> visitors) {
        this.visitors = visitors;
    }

    @Override
    public ProcessingResult read(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload) {
        Asserts.notNull(discriminator);

        final String discriminatorValue = discriminator.getDiscriminatorValue();

        Config config = Optional.ofNullable(configMap.get(discriminatorValue))
                .orElseThrow(() -> new IllegalArgumentException(StringFormatter.format("visitor not found. discriminatorValue: {}", discriminator)));

        final BatchWalker.Builder builder =
                BatchWalker.builder(config.valueObjectType)
                        .visitor(config.visitor)
                        .resource(resource)
                        .payload(payload)
                        .batchSize(config.batchSize)
                        .excelType(config.excelType)
                        .password(config.password)
                        .conversionService(conversionService)
                        .addIncludeSheet(SheetPredicateFactories.ofIndex(config.includeSheetSet.toArray(new Integer[0])))
                        .setValidators(validators)
                        .globalCellParser(config.globalCellParser);

        for (Pair<Integer, Integer> o : config.headers) {
            builder.setHeader(o.getA(), o.getB());
        }

        for (Pair<Integer, Set<Integer>> o : config.excludeRowSets) {
            builder.addExcludeRow(RowPredicateFactories.indexInSet(o.getA(), o.getB().toArray(new Integer[0])));
        }

        for (Tuple<Integer, Integer, Integer> o : config.excludeRowRanges) {
            builder.addExcludeRow(RowPredicateFactories.indexInRange(o.getA(), o.getB(), o.getC()));
        }

        for (Tuple<Integer, Integer, CellParser> o : config.columnBasedCellParsers) {
            builder.setColumnBasedCellParser(o.getA(), o.getB(), o.getC());
        }

        return builder.build()
                .walk();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        try {
            this.conversionService = applicationContext.getBean(ConversionService.class);
        } catch (BeansException e) {
            this.conversionService = new DefaultFormattingConversionService();
        }

        final List<Validator> validators = new ArrayList<>();

        try {
            validators.add(applicationContext.getBean(Validator.class));
        } catch (BeansException e) {
            validators.add(NullValidator.getInstance());
        }

        this.validators = validators.toArray(new Validator[0]);
    }

    @Override
    public void afterPropertiesSet() {
        for (BatchVisitor<?> visitor : visitors) {
            if (visitor != null) {
                Config config = parseConfig(visitor);
                if (config != null) {
                    configMap.put(config.discriminatorValue, config);
                    System.out.println("---");
                    System.out.println("---");
                    System.out.println(config);
                    System.out.println("---");
                    System.out.println("---");
                }
            }
        }
    }

    private Config parseConfig(@Nullable BatchVisitor<?> visitor) {
        if (visitor == null) {
            return null;
        }

        final Class<?> visitorType = visitor.getClass();

        final BatchProcessor primaryAnnotation =
                AnnotationUtils.findAnnotation(visitorType, BatchProcessor.class);

        if (primaryAnnotation == null) {
            return null;
        }

        final Config config = new Config();
        config.visitor = visitor;
        config.discriminatorValue = primaryAnnotation.discriminatorValue();
        config.valueObjectType = primaryAnnotation.valueObjectType();
        config.batchSize = getBatchSize(visitorType);
        config.headers = getHeader(visitorType);
        config.includeSheetSet = getIncludeSheetSet(visitorType);
        config.password = getPassword(visitorType);
        config.excelType = getExcelType(visitorType);
        config.excludeRowSets = getExcludeRowSets(visitorType);
        config.excludeRowRanges = getExcludeRowRanges(visitorType);
        config.globalCellParser = getGlobalCellParser(visitorType);
        config.columnBasedCellParsers = getColumnBasedCellParser(visitorType);
        return config;
    }

    private int getBatchSize(Class<?> visitorType) {
        final BatchSize annotation = AnnotationUtils.findAnnotation(visitorType, BatchSize.class);
        if (annotation == null) {
            return DEFAULT_BATCH_SIZE;
        }

        int value = annotation.value();
        if (value >= 1) {
            return value;
        } else {
            Object obj = ExpressionUtils.getValue(applicationContext, annotation.expression());
            if (obj == null) {
                throw new ClassCastException(StringFormatter.format("cannot cast null to int"));
            }
            if (obj instanceof Integer) {
                return (int) obj;
            }
            if (obj instanceof String) {
                return Integer.parseInt((String) obj);
            }
            throw new ClassCastException(StringFormatter.format("cannot cast {} to int", obj.getClass()));
        }
    }

    private List<Pair<Integer, Integer>> getHeader(Class<?> visitorType) {
        List<Pair<Integer, Integer>> headerConfig = new LinkedList<>();
        Header.List listAnnotation = AnnotationUtils.findAnnotation(visitorType, Header.List.class);

        if (listAnnotation != null) {
            for (Header annotation : listAnnotation.value()) {
                headerConfig.add(Pair.of(annotation.sheetIndex(), annotation.rowIndex()));
            }
        } else {
            Header annotation = AnnotationUtils.findAnnotation(visitorType, Header.class);
            if (annotation != null) {
                headerConfig.add(Pair.of(annotation.sheetIndex(), annotation.rowIndex()));
            }
        }
        return headerConfig;
    }

    private Set<Integer> getIncludeSheetSet(Class<?> visitorType) {
        IncludeSheetSet annotation = AnnotationUtils.findAnnotation(visitorType, IncludeSheetSet.class);
        if (annotation == null) {
            return Collections.emptySet();
        } else {
            Set<Integer> set = Arrays.stream(annotation.sheetIndexes()).boxed().collect(Collectors.toSet());
            return Collections.unmodifiableSet(set);
        }
    }

    private String getPassword(Class<?> visitorType) {
        Password annotation = AnnotationUtils.findAnnotation(visitorType, Password.class);
        if (annotation == null) {
            return null;
        } else {
            final String pwd = annotation.value();
            return StringUtils.isEmpty(pwd) ? null : pwd;
        }
    }

    private ExcelType getExcelType(Class<?> visitorType) {
        Type annotation = AnnotationUtils.findAnnotation(visitorType, Type.class);
        if (annotation == null) {
            return ExcelType.XSSF;
        } else {
            return annotation.value();
        }
    }

    private List<Pair<Integer, Set<Integer>>> getExcludeRowSets(Class<?> visitorType) {
        List<Pair<Integer, Set<Integer>>> list = new ArrayList<>();
        ExcludeRowSet.List listAnnotation = AnnotationUtils.findAnnotation(visitorType, ExcludeRowSet.List.class);
        if (listAnnotation != null) {
            for (ExcludeRowSet annotation : listAnnotation.value()) {
                Set<Integer> set = Arrays.stream(annotation.rowIndexes()).boxed().collect(Collectors.toSet());
                list.add(Pair.of(annotation.sheetIndex(), set));
            }
        } else {
            ExcludeRowSet annotation = AnnotationUtils.findAnnotation(visitorType, ExcludeRowSet.class);
            if (annotation != null) {
                Set<Integer> set = Arrays.stream(annotation.rowIndexes()).boxed().collect(Collectors.toSet());
                list.add(Pair.of(annotation.sheetIndex(), set));
            }
        }

        return Collections.unmodifiableList(list);
    }

    private List<Tuple<Integer, Integer, Integer>> getExcludeRowRanges(Class<?> visitorType) {
        List<Tuple<Integer, Integer, Integer>> list = new ArrayList<>();
        ExcludeRowRange.List listAnnotation = AnnotationUtils.findAnnotation(visitorType, ExcludeRowRange.List.class);
        if (listAnnotation != null) {
            for (ExcludeRowRange annotation : listAnnotation.value()) {
                list.add(
                        Tuple.of(
                                annotation.sheetIndex(),
                                annotation.minInclude(),
                                annotation.maxExclude()
                        )
                );
            }
        } else {
            ExcludeRowRange annotation = AnnotationUtils.findAnnotation(visitorType, ExcludeRowRange.class);
            if (annotation != null) {
                list.add(
                        Tuple.of(
                                annotation.sheetIndex(),
                                annotation.minInclude(),
                                annotation.maxExclude()
                        )
                );
            }
        }

        return Collections.unmodifiableList(list);
    }

    private GlobalCellParser getGlobalCellParser(Class<?> visitorType) {
        spring.turbo.module.excel.reader.annotation.GlobalCellParser annotation =
                AnnotationUtils.getAnnotation(visitorType, spring.turbo.module.excel.reader.annotation.GlobalCellParser.class);

        if (annotation != null) {
            return InstanceUtils.newInstanceOrThrow(annotation.type());
        } else {
            return new DefaultCellParser();
        }
    }

    private List<Tuple<Integer, Integer, CellParser>> getColumnBasedCellParser(Class<?> visitorType) {
        List<Tuple<Integer, Integer, CellParser>> list = new ArrayList<>();
        ColumnBasedCellParser.List listAnnotation = AnnotationUtils.findAnnotation(visitorType, ColumnBasedCellParser.List.class);
        if (listAnnotation != null) {
            for (ColumnBasedCellParser annotation : listAnnotation.value()) {
                list.add(
                        Tuple.of(
                                annotation.sheetIndex(),
                                annotation.columnIndex(),
                                InstanceUtils.newInstanceOrThrow(annotation.type())
                        )
                );
            }
        } else {
            ColumnBasedCellParser annotation = AnnotationUtils.findAnnotation(visitorType, ColumnBasedCellParser.class);
            if (annotation != null) {
                list.add(
                        Tuple.of(
                                annotation.sheetIndex(),
                                annotation.columnIndex(),
                                InstanceUtils.newInstanceOrThrow(annotation.type())
                        )
                );
            }
        }
        return Collections.unmodifiableList(list);
    }

    @ToString
    private static class Config {
        private BatchVisitor visitor;
        private String discriminatorValue;
        private Class<?> valueObjectType;
        private int batchSize;
        private List<Pair<Integer, Integer>> headers;
        private Set<Integer> includeSheetSet;
        private String password;
        private ExcelType excelType;
        private List<Pair<Integer, Set<Integer>>> excludeRowSets;
        private List<Tuple<Integer, Integer, Integer>> excludeRowRanges;
        private GlobalCellParser globalCellParser;
        private List<Tuple<Integer, Integer, CellParser>> columnBasedCellParsers;
    }

}
