/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.DataBinding;
import spring.turbo.bean.valueobject.NamedArray;
import spring.turbo.bean.valueobject.NullValidator;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.module.excel.AbortException;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.batch.Batch;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;
import spring.turbo.module.excel.config.AliasConfig;
import spring.turbo.module.excel.config.HeaderConfig;
import spring.turbo.module.excel.config.HeaderInfo;
import spring.turbo.module.excel.function.RowPredicate;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicate;
import spring.turbo.module.excel.function.SheetPredicateFactories;
import spring.turbo.module.excel.util.RowUtils;
import spring.turbo.module.excel.util.SheetUtils;
import spring.turbo.module.excel.visitor.BatchVisitor;
import spring.turbo.module.excel.visitor.NullBatchVisitor;
import spring.turbo.module.excel.visitor.ProcessingContext;
import spring.turbo.util.*;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class BatchWalker<T> extends AbstractBatchWalker {

    private final Map<Integer, HeaderInfo> headerInfoMap = new HashMap<>();
    private ProcessPayload payload;
    private Resource resource;
    private ExcelType excelType;
    private String password;
    private Batch<T> dataBatch;
    private BatchVisitor<T> visitor;
    private Supplier<T> valueObjectSupplier;
    private ConversionService conversionService;
    private List<Validator> validators;
    private SheetPredicate includeSheetPredicate;
    private RowPredicate excludeRowPredicate;
    private GlobalCellParser globalCellParser;
    private List<Tuple<Integer, Integer, CellParser>> cellParsers;
    private HeaderConfig headerConfig;
    private AliasConfig aliasConfig;

    /**
     * 构造方法
     */
    private BatchWalker() {
        super();
    }

    public static <T> Builder<T> builder(Class<T> valueObjectType) {
        return new Builder<>(valueObjectType);
    }

    public ProcessingResult walk() {
        Workbook workbook;
        try {
            workbook = super.createWorkbook(excelType, resource, password);
        } catch (Exception e) {
            visitor.onResourceOpeningError(resource, excelType, password, payload);
            CloseUtils.closeQuietly(resource);
            return ProcessingResult.RESOURCE_ERROR;
        }
        this.initHeaderInfo(workbook);

        try {
            doWalker(workbook);
            return ProcessingResult.NORMAL;
        } catch (AbortException e) {
            visitor.onAbort(payload);
            return ProcessingResult.ABORTED;
        } finally {
            super.close();
            CloseUtils.closeQuietly(workbook);
            CloseUtils.closeQuietly(resource);
        }
    }

    private void doWalker(Workbook workbook) throws AbortException {

        visitor.beforeProcessing(new ProcessingContext(resource, workbook, null, null), payload);

        for (Sheet sheet : workbook) {
            final int sheetIndex = SheetUtils.getIndex(sheet);
            if (includeSheetPredicate.test(sheet) && headerInfoMap.containsKey(sheetIndex)) {

                // 开始处理一个新sheet也需要判断是否中断
                if (visitor.shouldAbort(payload)) {
                    throw new AbortException();
                }

                final HeaderInfo headerInfo = headerInfoMap.get(sheetIndex);
                final String[] headerArray = headerInfo.getData();

                for (Row row : sheet) {

                    // 开始处理一个新sheet也需要判断是否中断
                    if (visitor.shouldAbort(payload)) {
                        throw new AbortException();
                    }

                    final int rowIndex = RowUtils.getIndex(row);

                    // 配置特意跳过某些行
                    if (excludeRowPredicate.test(sheet, row)) {
                        continue;
                    }

                    // 以下两种情况不处理
                    // 1. 本行位于头的上方
                    // 2. 本行正好是头
                    if (rowIndex <= headerInfo.getRowIndex() && sheetIndex == headerInfo.getSheetIndex()) {
                        continue;
                    }

                    // 转换成数据
                    final String[] dataArray = this.getRowData(row, headerArray.length, headerInfo.getFirstCellIndex());

                    // 数据实际无意义
                    if (ArrayUtils.doseNotContainsAnyElements(dataArray)) {
                        continue;
                    }

                    // 创建一个vo对象
                    T vo = valueObjectSupplier.get();

                    // 完成数据绑定和验证
                    final BindingResult bindingResult = DataBinding.newInstance()
                            .valueObject(vo)
                            .conversionService(conversionService)
                            .validators(validators.toArray(new Validator[0]))
                            .data(NamedArray.builder()
                                    .addNames(headerArray)
                                    .addObjects(dataArray)
                                    .build())
                            .bind();

                    if (bindingResult.hasErrors()) {
                        try {
                            payload.incrInvalidDataCount(1);    // 调整计数器
                            visitor.onInvalidValueObject(new ProcessingContext(resource, workbook, sheet, row), payload, vo, bindingResult);
                        } catch (Throwable e) {
                            if (e instanceof AbortException) {
                                throw e;
                            } else {
                                payload.incrErrorCount(1);
                                if (ExitPolicy.ABORT == visitor.onError(new ProcessingContext(resource, workbook, sheet, row), payload, e)) {
                                    throw new AbortException();
                                }
                            }
                        }
                    } else {
                        payload.incrSuccessCount(1);
                        if (dataBatch.isFull()) {
                            try {
                                visitor.onValidValueObject(new ProcessingContext(resource, workbook, sheet, null), payload, dataBatch);
                            } catch (Throwable e) {
                                if (e instanceof AbortException) {
                                    throw e;
                                } else {
                                    payload.incrErrorCount(1);
                                    if (ExitPolicy.ABORT == visitor.onError(new ProcessingContext(resource, workbook, sheet, null), payload, e)) {
                                        throw new AbortException();
                                    }
                                }
                            }
                            dataBatch.clear();
                        }
                        dataBatch.add(vo);
                    }
                }
            }

            if (dataBatch.isNotEmpty()) {
                try {
                    visitor.onValidValueObject(new ProcessingContext(resource, workbook, sheet, null), payload, dataBatch);
                } catch (Throwable e) {
                    if (e instanceof AbortException) {
                        throw e;
                    } else {
                        if (ExitPolicy.ABORT == visitor.onError(new ProcessingContext(resource, workbook, sheet, null), payload, e)) {
                            throw new AbortException();
                        }
                    }
                }
                dataBatch.clear();
            }
        }

        visitor.afterProcessed(payload);
    }

    private String[] getRowData(Row row, int headerSize, int firstCellIndex) {
        List<String> data = new ArrayList<>(headerSize);
        for (int i = firstCellIndex; i < headerSize + firstCellIndex; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                data.add(getEffCellParser(SheetUtils.getIndex(cell.getSheet()), cell.getColumnIndex()).convert(cell));
            } else {
                data.add(null);
            }
        }
        return data.toArray(new String[0]);
    }

    private CellParser getEffCellParser(int sheetIndex, int columnIndex) {
        for (Tuple<Integer, Integer, CellParser> tuple : this.cellParsers) {
            if (tuple.getA() == sheetIndex && tuple.getB() == columnIndex) {
                return tuple.getC();
            }
        }
        return this.globalCellParser;
    }

    private void initHeaderInfo(Workbook workbook) {
        for (Tuple<Integer, Integer, String[]> tuple : headerConfig.getSheetIndexFixedHeader()) {
            final int sheetIndex = tuple.getA();
            final int offset = tuple.getB();
            final String[] data = tuple.getC();

            Sheet sheet;
            try {
                sheet = workbook.getSheetAt(sheetIndex);
            } catch (IllegalArgumentException e) {
                sheet = null;
            }
            if (sheet != null) {
                HeaderInfo headerInfo = new HeaderInfo();
                headerInfo.setSheetName(sheet.getSheetName());
                headerInfo.setSheetIndex(sheetIndex);
                headerInfo.setRowIndex(-1);
                headerInfo.setFirstCellIndex(offset);
                headerInfo.setData(replaceWithAlias(data));
                headerInfoMap.put(SheetUtils.getIndex(sheet), headerInfo);
            }
        }

        for (Tuple<String, Integer, String[]> tuple : headerConfig.getSheetNameFixedHeader()) {
            final String sheetName = tuple.getA();
            final int offset = tuple.getB();
            final String[] data = tuple.getC();

            final Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                int sheetIndex = workbook.getSheetIndex(sheet);
                HeaderInfo headerInfo = new HeaderInfo();
                headerInfo.setSheetName(sheetName);
                headerInfo.setSheetIndex(sheetIndex);
                headerInfo.setRowIndex(-1);
                headerInfo.setFirstCellIndex(offset);
                headerInfo.setData(replaceWithAlias(data));
                headerInfoMap.put(SheetUtils.getIndex(sheet), headerInfo);
            }
        }

        for (Pair<Integer, Integer> pair : headerConfig.getSheetIndexConfig()) {
            final int sheetIndex = pair.getA();
            final int headerRowIndex = pair.getB();

            Sheet sheet;
            try {
                sheet = workbook.getSheetAt(sheetIndex);
            } catch (IllegalArgumentException e) {
                sheet = null;
            }
            if (sheet != null) {
                Row row = sheet.getRow(headerRowIndex);
                if (row != null) {
                    int offset = row.getFirstCellNum();
                    final List<String> data = new ArrayList<>();
                    for (Cell cell : row) {
                        data.add(globalCellParser.convert(cell));
                    }
                    HeaderInfo headerInfo = new HeaderInfo();
                    headerInfo.setSheetName(sheet.getSheetName());
                    headerInfo.setSheetIndex(sheetIndex);
                    headerInfo.setRowIndex(headerRowIndex);
                    headerInfo.setFirstCellIndex(offset);
                    headerInfo.setData(replaceWithAlias(data.toArray(new String[0])));
                    headerInfoMap.put(SheetUtils.getIndex(sheet), headerInfo);
                }
            }
        }

        for (Pair<String, Integer> pair : headerConfig.getSheetNameConfig()) {
            final String sheetName = pair.getA();
            final int headerRowIndex = pair.getB();

            final Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                Row row = sheet.getRow(headerRowIndex);
                if (row != null) {
                    int offset = row.getFirstCellNum();
                    final List<String> data = new ArrayList<>();
                    for (Cell cell : row) {
                        data.add(globalCellParser.convert(cell));
                    }
                    HeaderInfo headerInfo = new HeaderInfo();
                    headerInfo.setSheetName(sheetName);
                    headerInfo.setSheetIndex(workbook.getSheetIndex(sheet));
                    headerInfo.setRowIndex(headerRowIndex);
                    headerInfo.setFirstCellIndex(offset);
                    headerInfo.setData(replaceWithAlias(data.toArray(new String[0])));
                    headerInfoMap.put(SheetUtils.getIndex(sheet), headerInfo);
                }
            }
        }
    }

    private String[] replaceWithAlias(String[] header) {
        if (this.aliasConfig == null) {
            return header;
        }

        for (int i = 0; i < header.length; i++) {
            header[i] = aliasConfig.getOrDefault(header[i], header[i]);
        }
        return header;
    }

    /**
     * 创建器
     */
    public final static class Builder<T> {
        private final Class<T> valueObjectType;
        private final Supplier<T> valueObjectSupplier;
        private final AliasConfig aliasConfig = AliasConfig.newInstance();
        private final HeaderConfig headerConfig = HeaderConfig.newInstance();
        private final List<SheetPredicate> includeSheetPredicates = new LinkedList<>();
        private final List<RowPredicate> excludeSheetPredicates = new LinkedList<>();
        private final List<Tuple<Integer, Integer, CellParser>> cellParsers = new LinkedList<>();
        private final List<Validator> validators = new LinkedList<>();
        private BatchVisitor<T> visitor;
        private ProcessPayload payload;
        private int batchSize = 1000;
        private ExcelType excelType;
        private Resource resource;
        private String password;
        private GlobalCellParser globalCellParser;
        private ConversionService conversionService;

        private Builder(Class<T> valueObjectType) {
            Asserts.notNull(valueObjectType);
            this.valueObjectType = valueObjectType;
            this.valueObjectSupplier = new ReflectionObjectSupplier<>(valueObjectType);
        }

        public Builder<T> batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder<T> payload(ProcessPayload payload) {
            this.payload = payload;
            return this;
        }

        public Builder<T> setAlias(String from, String to) {
            this.aliasConfig.add(from, to);
            return this;
        }

        public Builder<T> setHeader(int sheetIndex, int rowIndex) {
            this.headerConfig.bySheetIndex(sheetIndex, rowIndex);
            return this;
        }

        public Builder<T> addIncludeSheet(SheetPredicate... predicates) {
            if (predicates != null) {
                for (SheetPredicate predicate : predicates) {
                    Optional.ofNullable(predicate).ifPresent(includeSheetPredicates::add);
                }
            }
            return this;
        }

        public Builder<T> addExcludeRow(RowPredicate... predicates) {
            if (predicates != null) {
                for (RowPredicate predicate : predicates) {
                    Optional.ofNullable(predicate).ifPresent(excludeSheetPredicates::add);
                }
            }
            return this;
        }

        public Builder<T> visitor(BatchVisitor<T> visitor) {
            this.visitor = visitor;
            return this;
        }

        public Builder<T> visitor(Class<? extends BatchVisitor<T>> visitorType) {
            this.visitor = InstanceUtils.newInstanceOrThrow(visitorType);
            return this;
        }

        public Builder<T> excelType(ExcelType excelType) {
            this.excelType = excelType;
            return this;
        }

        public Builder<T> resource(Resource resource) {
            this.resource = resource;
            return this;
        }

        public Builder<T> resource(File file) {
            this.resource = new FileSystemResource(file);
            return this;
        }

        public Builder<T> resource(Path path) {
            this.resource = new FileSystemResource(path);
            return this;
        }

        public Builder<T> password(String password) {
            this.password = password;
            return this;
        }

        public Builder<T> globalCellParser(GlobalCellParser parser) {
            this.globalCellParser = parser;
            return this;
        }

        public Builder<T> setColumnBasedCellParser(int sheetIndex, int columnIndex, CellParser parser) {
            this.cellParsers.add(Tuple.of(sheetIndex, columnIndex, parser));
            return this;
        }

        public Builder<T> conversionService(ConversionService conversionService) {
            this.conversionService = conversionService;
            return this;
        }

        public Builder<T> setValidators(Validator... validators) {
            if (validators != null) {
                for (Validator validator : validators) {
                    if (validator != null) {
                        this.validators.add(validator);
                    }
                }
            }
            return this;
        }


        public BatchWalker<T> build() {
            // 合并alias
            // 反射得到的Alias优先级更高
            this.aliasConfig.putAll(ValueObjectUtils.getAliases(valueObjectType));

            final BatchWalker<T> walker = new BatchWalker<>();
            walker.valueObjectSupplier = valueObjectSupplier;
            walker.dataBatch = new Batch<>(batchSize);
            walker.payload = Optional.ofNullable(payload).orElseGet(ProcessPayload::newInstance);
            walker.excelType = Optional.ofNullable(excelType).orElse(ExcelType.XSSF);
            walker.resource = Optional.ofNullable(resource).orElseThrow(() -> new NullPointerException("resource not set"));
            walker.password = StringUtils.isEmpty(password) ? null : password;
            walker.headerConfig = headerConfig;
            walker.aliasConfig = aliasConfig;
            walker.globalCellParser = Optional.ofNullable(globalCellParser).orElseGet(DefaultCellParser::new);
            walker.cellParsers = this.cellParsers;
            walker.conversionService = Optional.ofNullable(conversionService).orElseGet(DefaultFormattingConversionService::new);
            walker.validators = CollectionUtils.isEmpty(validators) ? Collections.singletonList(NullValidator.getInstance()) : validators;
            walker.includeSheetPredicate = CollectionUtils.isEmpty(includeSheetPredicates) ?
                    SheetPredicateFactories.alwaysTrue() : SheetPredicateFactories.any(includeSheetPredicates.toArray(new SheetPredicate[0]));
            walker.excludeRowPredicate = CollectionUtils.isEmpty(excludeSheetPredicates) ?
                    RowPredicateFactories.alwaysFalse() : RowPredicateFactories.any(excludeSheetPredicates.toArray(new RowPredicate[0]));
            walker.visitor = Optional.ofNullable(visitor).orElseGet(NullBatchVisitor::new);

            return walker;
        }
    }

}
