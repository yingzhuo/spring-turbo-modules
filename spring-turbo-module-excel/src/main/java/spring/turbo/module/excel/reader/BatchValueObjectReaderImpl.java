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
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Validator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.Alias;
import spring.turbo.bean.valueobject.NullValidator;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.core.SpringContext;
import spring.turbo.core.SpringContextAware;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;
import spring.turbo.module.excel.config.AliasConfig;
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

import static spring.turbo.util.StringPool.ANNOTATION_STRING_NULL;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
class BatchValueObjectReaderImpl implements BatchValueObjectReader, InitializingBean, SpringContextAware {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final List<BatchVisitor<?>> visitors;
    private final Map<String, Config> configMap = new HashMap<>();

    private InstanceCache instanceCache;
    private ApplicationContext applicationContext;
    private ConversionService conversionService;
    private Validator primaryValidator;

    public BatchValueObjectReaderImpl(List<BatchVisitor<?>> visitors) {
        this.visitors = visitors;
    }

    @Override
    public ProcessingResult read(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload) {
        Asserts.notNull(discriminator);

        final String discriminatorValue = discriminator.getDiscriminatorValue();

        Config config = Optional.ofNullable(configMap.get(discriminatorValue))
                .orElseThrow(() -> new IllegalArgumentException(StringFormatter.format("visitor not found. discriminatorValue: {}", discriminator)));

        BatchWalker.Builder builder =
                BatchWalker.builder(config.valueObjectType)
                        .visitor(config.visitor)
                        .resource(resource)
                        .payload(payload)
                        .batchSize(config.batchSize)
                        .excelType(config.excelType)
                        .password(config.password)
                        .conversionService(conversionService)
                        .setValidators(primaryValidator)
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

        if (!CollectionUtils.isEmpty(config.includeSheetSet)) {
            builder.addIncludeSheet(SheetPredicateFactories.ofIndex(config.includeSheetSet.toArray(new Integer[0])));
        }

        if (StringUtils.isNotBlank(config.includeSheetPattern)) {
            builder.addIncludeSheet(SheetPredicateFactories.ofNamePattern(config.includeSheetPattern));
        }

        if (!CollectionUtils.isEmpty(config.aliasConfig)) {
            for (String from : config.aliasConfig.keySet()) {
                String to = config.aliasConfig.get(from);
                builder.setAlias(from, to);
            }
        }

        if (!CollectionUtils.isEmpty(config.additionalValidators)) {
            config.additionalValidators.forEach(builder::setValidators);
        }

        if (config.builderCustomizer != null) {
            builder = config.builderCustomizer.customize(builder);
        }

        return builder.build().walk();
    }

    @Override
    public void setSpringContext(SpringContext springContext) {
        this.applicationContext = springContext.getApplicationContext();
        this.conversionService = springContext.getBean(ConversionService.class).orElseGet(DefaultFormattingConversionService::new);
        this.primaryValidator = springContext.getBean(Validator.class).orElseGet(NullValidator::getInstance);
        this.instanceCache = InstanceCache.newInstance(springContext.getApplicationContext());
    }

    @Override
    public void afterPropertiesSet() {
        for (BatchVisitor<?> visitor : visitors) {
            if (visitor != null) {
                Config config = parseConfig(visitor);
                if (config != null) {
                    configMap.put(config.discriminatorValue, config);
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
        config.includeSheetPattern = getIncludeSheetPattern(visitorType);
        config.password = getPassword(visitorType);
        config.excelType = getExcelType(visitorType);
        config.excludeRowSets = getExcludeRowSets(visitorType);
        config.excludeRowRanges = getExcludeRowRanges(visitorType);
        config.globalCellParser = getGlobalCellParser(visitorType);
        config.columnBasedCellParsers = getColumnBasedCellParser(visitorType);
        config.aliasConfig = getAliasConfig(visitorType);
        config.additionalValidators = getAdditionalValidators(visitorType);
        config.builderCustomizer = getBuilderCustomizer(visitorType);
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
        List<Pair<Integer, Integer>> headerConfig = new ArrayList<>();
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

    private String getIncludeSheetPattern(Class<?> visitorType) {
        IncludeSheetPattern annotation = AnnotationUtils.findAnnotation(visitorType, IncludeSheetPattern.class);
        return annotation != null ? annotation.value() : null;
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
                AnnotationUtils.findAnnotation(visitorType, spring.turbo.module.excel.reader.annotation.GlobalCellParser.class);

        if (annotation != null) {
            return instanceCache.findOrCreate(annotation.type());
        } else {
            return new DefaultCellParser();
        }
    }

    private AliasConfig getAliasConfig(Class<?> visitorType) {
        AliasConfig aliasConfig = AliasConfig.newInstance();

        Alias.List listAnnotation = AnnotationUtils.findAnnotation(visitorType, Alias.List.class);
        if (listAnnotation != null) {
            for (Alias annotation : listAnnotation.value()) {
                final String from = annotation.from();
                final String to = annotation.to();
                if (!ANNOTATION_STRING_NULL.equals(from) && !ANNOTATION_STRING_NULL.equals(to)) {
                    aliasConfig.put(from, to);
                }
            }
        } else {
            Alias annotation = AnnotationUtils.findAnnotation(visitorType, Alias.class);
            if (annotation != null) {
                final String from = annotation.from();
                final String to = annotation.to();
                if (!ANNOTATION_STRING_NULL.equals(from) && !ANNOTATION_STRING_NULL.equals(to)) {
                    aliasConfig.put(from, to);
                }
            }
        }

        // valueObjectType 指定的Alias会又walker搞定
        return aliasConfig;
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
                                instanceCache.findOrCreate(annotation.type())
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
                                instanceCache.findOrCreate(annotation.type())
                        )
                );
            }
        }
        return Collections.unmodifiableList(list);
    }

    private List<Validator> getAdditionalValidators(Class<?> visitorType) {
        AdditionalValidators annotation = AnnotationUtils.findAnnotation(visitorType, AdditionalValidators.class);
        if (annotation != null && !ArrayUtils.isNullOrEmpty(annotation.value())) {
            return Arrays.stream(annotation.value())
                    .map(type -> (Validator) instanceCache.findOrCreate(type))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private BuilderCustomizer getBuilderCustomizer(Class<?> visitorType) {
        Customizer customizer = AnnotationUtils.findAnnotation(visitorType, Customizer.class);
        return customizer != null ? instanceCache.findOrCreate(customizer.value()) : null;
    }


    private static class Config {
        private BatchVisitor visitor;
        private String discriminatorValue;
        private Class<?> valueObjectType;
        private int batchSize;
        private List<Pair<Integer, Integer>> headers;
        private Set<Integer> includeSheetSet;
        private String includeSheetPattern;
        private String password;
        private ExcelType excelType;
        private List<Pair<Integer, Set<Integer>>> excludeRowSets;
        private List<Tuple<Integer, Integer, Integer>> excludeRowRanges;
        private GlobalCellParser globalCellParser;
        private List<Tuple<Integer, Integer, CellParser>> columnBasedCellParsers;
        private AliasConfig aliasConfig;
        private List<Validator> additionalValidators;
        private BuilderCustomizer builderCustomizer;
    }

}
