/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import spring.turbo.bean.Payload;
import spring.turbo.bean.valueobject.DataBinding;
import spring.turbo.bean.valueobject.NamedArray;
import spring.turbo.lang.Immutable;
import spring.turbo.module.excel.*;
import spring.turbo.module.excel.func.RowPredicate;
import spring.turbo.module.excel.func.RowPredicateFactories;
import spring.turbo.module.excel.func.SheetPredicate;
import spring.turbo.module.excel.func.SheetPredicateFactories;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class ValueObjectReadingWalkerBuilder<T> {

    private final Supplier<T> valueObjectSupplier;
    private final List<Validator> validators = new LinkedList<>();
    private final List<SheetPredicate> walkIncludeSheetPredicates = new LinkedList<>();
    private final List<RowPredicate> interceptorExcludeRowPredicate = new LinkedList<>();
    private final HeaderConfig headerConfig = HeaderConfig.newInstance();
    private final AliasConfig aliasConfig = AliasConfig.newInstance();
    private ConversionService conversionService;
    private CellParser cellParser = new DefaultCellParser();
    private boolean skipAllNullData = true;
    private Consumer<Context<T>> onSuccessConsumer = t -> {
    };
    private Consumer<Context<T>> onErrorConsumer = t -> {
    };

    private ValueObjectReadingWalkerBuilder(Supplier<T> valueObjectSupplier) {
        this.valueObjectSupplier = valueObjectSupplier;
    }

    public static <T> ValueObjectReadingWalkerBuilder<T> newInstance(Supplier<T> supplier) {
        Asserts.notNull(supplier);
        return new ValueObjectReadingWalkerBuilder<>(supplier);
    }

    public ValueObjectReadingWalkerBuilder<T> conversionService(ConversionService conversionService) {
        Asserts.notNull(conversionService);
        this.conversionService = conversionService;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> validators(Validator... validators) {
        if (validators != null) {
            Arrays.stream(validators).filter(Objects::nonNull).forEach(this.validators::add);
        }
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> readSheet(String... sheetNames) {
        if (sheetNames != null) {
            for (String sheetName : sheetNames) {
                if (sheetName != null) {
                    this.walkIncludeSheetPredicates.add(SheetPredicateFactories.ofName(sheetName));
                }
            }
        }
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> readSheet(int... sheetIndexes) {
        if (sheetIndexes != null) {
            for (int sheetIndex : sheetIndexes) {
                if (sheetIndex >= 0) {
                    this.walkIncludeSheetPredicates.add(SheetPredicateFactories.ofIndex(sheetIndex));
                }
            }
        }
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> excludeRowInSet(String sheetName, Integer... rowIndexes) {
        this.interceptorExcludeRowPredicate.add(RowPredicateFactories.indexInSet(sheetName, rowIndexes));
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> excludeRowInSet(int sheetIndex, Integer... rowIndexes) {
        this.interceptorExcludeRowPredicate.add(RowPredicateFactories.indexInSet(sheetIndex, rowIndexes));
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> excludeRowInRange(String sheetName, int minInclude, int maxInclude) {
        this.interceptorExcludeRowPredicate.add(RowPredicateFactories.indexInRange(sheetName, minInclude, maxInclude));
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> excludeRowInRange(int sheetIndex, int minInclude, int maxInclude) {
        this.interceptorExcludeRowPredicate.add(RowPredicateFactories.indexInRange(sheetIndex, minInclude, maxInclude));
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> cellParser(CellParser cellParser) {
        Asserts.notNull(cellParser);
        this.cellParser = cellParser;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> alias(String from, String to) {
        this.aliasConfig.add(from, to);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> alias(String from1, String to1, String from2, String to2) {
        this.aliasConfig.add(from1, to1);
        this.aliasConfig.add(from2, to2);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> alias(String from1, String to1, String from2, String to2, String from3, String to3) {
        this.aliasConfig.add(from1, to1);
        this.aliasConfig.add(from2, to2);
        this.aliasConfig.add(from3, to3);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> alias(String from1, String to1, String from2, String to2, String from3, String to3, String from4, String to4) {
        this.aliasConfig.add(from1, to1);
        this.aliasConfig.add(from2, to2);
        this.aliasConfig.add(from3, to3);
        this.aliasConfig.add(from4, to4);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> alias(String from1, String to1,
                                                    String from2, String to2,
                                                    String from3, String to3,
                                                    String from4, String to4,
                                                    String from5, String to5) {
        this.aliasConfig.add(from1, to1);
        this.aliasConfig.add(from2, to2);
        this.aliasConfig.add(from3, to3);
        this.aliasConfig.add(from4, to4);
        this.aliasConfig.add(from5, to5);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> alias(String from1, String to1,
                                                    String from2, String to2,
                                                    String from3, String to3,
                                                    String from4, String to4,
                                                    String from5, String to5,
                                                    String from6, String to6) {
        this.aliasConfig.add(from1, to1);
        this.aliasConfig.add(from2, to2);
        this.aliasConfig.add(from3, to3);
        this.aliasConfig.add(from4, to4);
        this.aliasConfig.add(from5, to5);
        this.aliasConfig.add(from6, to6);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> sheetHeader(String sheetName, int rowIndex) {
        this.headerConfig.bySheetName(sheetName, rowIndex);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> sheetHeader(int sheetIndex, int rowIndex) {
        this.headerConfig.bySheetIndex(sheetIndex, rowIndex);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> sheetHeader(String sheetName, String... header) {
        this.headerConfig.fixed(sheetName, header);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> sheetHeader(int sheetIndex, String... header) {
        this.headerConfig.fixed(sheetIndex, header);
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> onSuccess(Consumer<Context<T>> consumer) {
        Asserts.notNull(consumer);
        this.onSuccessConsumer = consumer;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> onError(Consumer<Context<T>> consumer) {
        Asserts.notNull(consumer);
        this.onErrorConsumer = consumer;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> skipAllNullData(boolean skipAllNullData) {
        this.skipAllNullData = skipAllNullData;
        return this;
    }

    public ExcelWalker build(ExcelType type, Resource excel) {
        Asserts.notNull(type);
        Asserts.notNull(excel);

        final ExcelWalkerInterceptor interceptor = new AbstractReadingExcelWalkerInterceptor(this.headerConfig) {

            {
                super.setCellParser(cellParser);
                super.setAliasConfig(aliasConfig);
                if (!interceptorExcludeRowPredicate.isEmpty()) {
                    super.setExcludeRowPredicate(RowPredicateFactories.any(interceptorExcludeRowPredicate.toArray(new RowPredicate[0])));
                }
            }

            private boolean allNull(String[] array) {
                if (array == null) return true;
                for (String x : array) {
                    if (x != null) return false;
                }
                return true;
            }

            @Override
            protected void doOnRow(Workbook workbook, Sheet sheet, Row row, Payload payload, String[] header, String[] rowData) {
                T vo = valueObjectSupplier.get();

                if (vo == null) {
                    return;
                }

                if (skipAllNullData && allNull(rowData)) {
                    return;
                }

                final DataBinding dataBinding = DataBinding.<T>newInstance()
                        .valueObject(vo)
                        .data(NamedArray.builder()
                                .addNames(header)
                                .addObjects(rowData)
                                .build())
                        .conversionService(Optional.ofNullable(conversionService).orElse(new DefaultFormattingConversionService()));

                if (!validators.isEmpty()) {
                    dataBinding.validators(validators.toArray(new Validator[0]));
                }

                final BindingResult bindingResult = dataBinding.bind();

                if (bindingResult.hasErrors()) {
                    onErrorConsumer.accept(new Context<>(excel, workbook, sheet, row, (T) null, bindingResult));
                } else {
                    onSuccessConsumer.accept(new Context<>(excel, workbook, sheet, row, vo, bindingResult));
                }
            }
        };

        ExcelWalker.Builder builder = ExcelWalker.builder()
                .type(type)
                .interceptor(interceptor);

        if (!walkIncludeSheetPredicates.isEmpty()) {
            builder.sheetPredicates(walkIncludeSheetPredicates.toArray(new SheetPredicate[0]));
        } else {
            builder.sheetPredicates(SheetPredicateFactories.alwaysTrue());
        }

        return builder.build(excel);
    }

    @Immutable
    public static class Context<T> implements Serializable {
        private final Resource resource;
        private final Workbook workbook;
        private final Sheet sheet;
        private final Row row;
        private final T objectValue;
        private final BindingResult bindingResult;

        public Context(Resource resource, Workbook workbook, Sheet sheet, Row row, T objectValue, BindingResult bindingResult) {
            this.resource = resource;
            this.workbook = workbook;
            this.sheet = sheet;
            this.row = row;
            this.objectValue = objectValue;
            this.bindingResult = bindingResult;
        }

        public Resource getResource() {
            return resource;
        }

        public Workbook getWorkbook() {
            return workbook;
        }

        public Sheet getSheet() {
            return sheet;
        }

        public Row getRow() {
            return row;
        }

        public T getObjectValue() {
            return objectValue;
        }

        public BindingResult getBindingResult() {
            return bindingResult;
        }
    }

}
