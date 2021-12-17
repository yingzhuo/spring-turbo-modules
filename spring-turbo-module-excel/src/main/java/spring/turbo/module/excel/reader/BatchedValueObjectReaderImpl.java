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
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;
import spring.turbo.module.excel.reader.annotation.ExcludeRowRange;
import spring.turbo.module.excel.reader.annotation.ExcludeRowSet;
import spring.turbo.module.excel.reader.annotation.Header;
import spring.turbo.module.excel.reader.annotation.*;
import spring.turbo.module.excel.visitor.BatchedVisitor;
import spring.turbo.util.Asserts;
import spring.turbo.util.ExpressionUtils;
import spring.turbo.util.StringFormatter;
import spring.turbo.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 应卓
 * @since 1.0.0
 */
class BatchedValueObjectReaderImpl implements BatchedValueObjectReader, InitializingBean, ApplicationContextAware {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final List<BatchedVisitor<?>> visitors;
    private final Map<String, Config> configMap = new HashMap<>();

    private ApplicationContext applicationContext;

    public BatchedValueObjectReaderImpl(List<BatchedVisitor<?>> visitors) {
        this.visitors = visitors;
    }

    @Override
    public ProcessingResult read(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload) {
        Asserts.notNull(discriminator);

        final String discriminatorValue = discriminator.getDiscriminatorValue();

        return ProcessingResult.ABORTED;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        for (BatchedVisitor<?> visitor : visitors) {
            if (visitor != null) {
                Config config = parseConfig(visitor);
                if (config != null) {
                    configMap.put(config.discriminatorValue, config);
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println(config);
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println("------");
                    System.out.println("------");
                }
            }
        }
    }

    private Config parseConfig(@Nullable BatchedVisitor<?> visitor) {
        if (visitor == null) {
            return null;
        }

        final Class<?> visitorType = visitor.getClass();

        final BatchedProcessor primaryAnnotation =
                AnnotationUtils.findAnnotation(visitorType, BatchedProcessor.class);

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
        config.globalCellParserType = getGlobalCellParserType(visitorType);
        config.columnBasedCellParserTypes = getColumnBasedCellParserTypes(visitorType);
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

    private Class<? extends GlobalCellParser> getGlobalCellParserType(Class<?> visitorType) {
        spring.turbo.module.excel.reader.annotation.GlobalCellParser annotation =
                AnnotationUtils.getAnnotation(visitorType, spring.turbo.module.excel.reader.annotation.GlobalCellParser.class);

        if (annotation != null) {
            return annotation.value();
        } else {
            return DefaultCellParser.class;
        }
    }

    private List<Tuple<Integer, Integer, Class<? extends CellParser>>> getColumnBasedCellParserTypes(Class<?> visitorType) {
        List<Tuple<Integer, Integer, Class<? extends CellParser>>> list = new ArrayList<>();
        ColumnBasedCellParser.List listAnnotation = AnnotationUtils.findAnnotation(visitorType, ColumnBasedCellParser.List.class);
        if (listAnnotation != null) {
            for (ColumnBasedCellParser annotation : listAnnotation.value()) {
                list.add(
                        Tuple.of(
                                annotation.sheetIndex(),
                                annotation.columnIndex(),
                                annotation.type()
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
                                annotation.type()
                        )
                );
            }
        }
        return Collections.unmodifiableList(list);
    }

    @ToString
    private static class Config {
        private BatchedVisitor<?> visitor;
        private String discriminatorValue;
        private Class<?> valueObjectType;
        private int batchSize;
        private List<Pair<Integer, Integer>> headers;
        private Set<Integer> includeSheetSet;
        private String password;
        private ExcelType excelType;
        private List<Pair<Integer, Set<Integer>>> excludeRowSets;
        private List<Tuple<Integer, Integer, Integer>> excludeRowRanges;
        private Class<? extends GlobalCellParser> globalCellParserType;
        private List<Tuple<Integer, Integer, Class<? extends CellParser>>> columnBasedCellParserTypes;
    }

}
