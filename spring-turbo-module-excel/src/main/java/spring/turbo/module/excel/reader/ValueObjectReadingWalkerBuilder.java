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
import spring.turbo.bean.valueobject.DataBinding;
import spring.turbo.bean.valueobject.NamedArray;
import spring.turbo.bean.valueobject.ReflectionObjectSupplier;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.module.excel.*;
import spring.turbo.module.excel.context.ErrorContext;
import spring.turbo.module.excel.context.InvalidDataContext;
import spring.turbo.module.excel.context.SuccessContext;
import spring.turbo.module.excel.function.RowPredicate;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicate;
import spring.turbo.module.excel.function.SheetPredicateFactories;
import spring.turbo.util.Asserts;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class ValueObjectReadingWalkerBuilder<T> {

    private final Class<T> valueObjectType;
    private final Supplier<T> valueObjectSupplier;
    private final List<Validator> validators = new LinkedList<>();
    private final List<SheetPredicate> walkerIncludeSheetPredicates = new LinkedList<>();
    private final List<RowPredicate> interceptorExcludeRowPredicate = new LinkedList<>();
    private final HeaderConfig headerConfig = HeaderConfig.newInstance();
    private final AliasConfig aliasConfig = AliasConfig.newInstance();
    private Supplier<ProcessPayload> payloadSupplier = ProcessPayload::newInstance;
    private ConversionService conversionService;
    private CellParser cellParser = new DefaultCellParser();
    private boolean skipAllNullData = true;
    private Consumer<SuccessContext<T>> onSuccessConsumer = ctx -> {
    };
    private Consumer<InvalidDataContext<T>> onInvalidDataConsumer = ctx -> {
    };
    private Function<ErrorContext, ExitPolicy> onThrowableFunction = ctx -> ExitPolicy.CONTINUE;

    private ValueObjectReadingWalkerBuilder(Supplier<T> valueObjectSupplier, Class<T> valueObjectType) {
        Asserts.notNull(valueObjectSupplier);
        Asserts.notNull(valueObjectType);

        this.valueObjectSupplier = valueObjectSupplier;
        this.valueObjectType = valueObjectType;
    }

    public static <T> ValueObjectReadingWalkerBuilder<T> newInstance(Class<T> valueObjectType) {
        return new ValueObjectReadingWalkerBuilder<>(ReflectionObjectSupplier.of(valueObjectType), valueObjectType);
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

    public ValueObjectReadingWalkerBuilder<T> onlyReadSheetPattern(String regex) {
        this.walkerIncludeSheetPredicates.add(SheetPredicateFactories.ofNamePattern(regex));
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> onlyReadSheet(String... sheetNames) {
        if (sheetNames != null) {
            for (String sheetName : sheetNames) {
                if (sheetName != null) {
                    this.walkerIncludeSheetPredicates.add(SheetPredicateFactories.ofName(sheetName));
                }
            }
        }
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> onlyReadSheet(Integer... sheetIndexes) {
        if (sheetIndexes != null) {
            for (int sheetIndex : sheetIndexes) {
                if (sheetIndex >= 0) {
                    this.walkerIncludeSheetPredicates.add(SheetPredicateFactories.ofIndex(sheetIndex));
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

    public ValueObjectReadingWalkerBuilder<T> payload(ProcessPayload payload) {
        return payloadSupplier(() -> payload);
    }

    public ValueObjectReadingWalkerBuilder<T> payloadSupplier(Supplier<ProcessPayload> payloadSupplier) {
        Asserts.notNull(payloadSupplier);
        this.payloadSupplier = payloadSupplier;
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

    public ValueObjectReadingWalkerBuilder<T> onSuccess(Consumer<SuccessContext<T>> consumer) {
        Asserts.notNull(consumer);
        this.onSuccessConsumer = consumer;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> onInvalidData(Consumer<InvalidDataContext<T>> consumer) {
        Asserts.notNull(consumer);
        this.onInvalidDataConsumer = consumer;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> onError(Function<ErrorContext, ExitPolicy> function) {
        Asserts.notNull(function);
        this.onThrowableFunction = function;
        return this;
    }

    public ValueObjectReadingWalkerBuilder<T> skipAllNullData(boolean skipAllNullData) {
        this.skipAllNullData = skipAllNullData;
        return this;
    }

    public ExcelWalker build(Resource excel) {
        return build(ExcelType.XSSF, excel);
    }

    public ExcelWalker build(ExcelType type, Resource excel) {
        Asserts.notNull(type);
        Asserts.notNull(excel);

        mergeAliases();

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
            protected void doOnRow(Workbook workbook, Sheet sheet, Row row, ProcessPayload payload, String[] header, String[] rowData) {
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
                    onInvalidDataConsumer.accept(new InvalidDataContext<>(payload, excel, workbook, sheet, row, vo, bindingResult));
                } else {
                    onSuccessConsumer.accept(new SuccessContext<>(payload, excel, workbook, sheet, row, vo));
                }
            }

            @Override
            public ExitPolicy onThrowable(Workbook workbook, Sheet sheet, Row row, ProcessPayload payload, Throwable throwable) {
                return onThrowableFunction.apply(new ErrorContext(payload, excel, workbook, sheet, row, throwable));
            }
        };

        ExcelWalker.Builder builder = ExcelWalker.builder()
                .type(type)
                .payloadSupplier(payloadSupplier)
                .interceptor(interceptor);

        if (!walkerIncludeSheetPredicates.isEmpty()) {
            builder.sheetPredicates(walkerIncludeSheetPredicates.toArray(new SheetPredicate[0]));
        } else {
            builder.sheetPredicates(SheetPredicateFactories.alwaysTrue());
        }

        return builder.build(excel);
    }

    private void mergeAliases() {
        final Map<String, String> aliases = ValueObjectUtils.getAliases(this.valueObjectType);
        this.aliasConfig.putAll(aliases);
    }

}
