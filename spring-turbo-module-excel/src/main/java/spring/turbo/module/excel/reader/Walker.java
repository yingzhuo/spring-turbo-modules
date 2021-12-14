/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.apache.poi.UnsupportedFileFormatException;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.DataBinding;
import spring.turbo.bean.valueobject.NamedArray;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.module.excel.AbortException;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;
import spring.turbo.module.excel.config.AliasConfig;
import spring.turbo.module.excel.config.HeaderConfig;
import spring.turbo.module.excel.config.HeaderInfo;
import spring.turbo.module.excel.function.RowPredicate;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicate;
import spring.turbo.module.excel.util.SheetUtils;
import spring.turbo.module.excel.visitor.Visitor;
import spring.turbo.module.excel.visitor.VisitorContext;
import spring.turbo.util.InstanceUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class Walker {

    private final ExcelType excelType;
    private final Resource resource;
    private final Class<?> valueObjectType;
    private final SheetPredicate includeSheetPredicate;
    private final HeaderConfig headerConfig;
    private final AliasConfig aliasConfig;
    private final GlobalCellParser globalCellParser;
    private final ProcessPayload payload;
    private final ConversionService conversionService;
    private final List<Validator> validators;
    private final Visitor visitor;
    private final Map<String, HeaderInfo> headerInfoMap = new HashMap<>();
    private final List<Tuple<Integer, Integer, CellParser>> cellParsers;
    private final String password;
    private final boolean excludeAllNullRow;
    private RowPredicate excludeRowPredicate; // 不是final还需要微调
    private POIFSFileSystem fileSystem;

    Walker(ExcelType excelType, Resource resource, Class<?> valueObjectType, SheetPredicate includeSheetPredicate, RowPredicate excludeRowPredicate, HeaderConfig headerConfig, AliasConfig aliasConfig, GlobalCellParser cellParser, List<Tuple<Integer, Integer, CellParser>> cellParsers, ProcessPayload payload, ConversionService conversionService, List<Validator> validators, Visitor visitor, String password, boolean excludeAllNullRow) {
        this.excelType = excelType;
        this.resource = resource;
        this.valueObjectType = valueObjectType;
        this.includeSheetPredicate = includeSheetPredicate;
        this.excludeRowPredicate = excludeRowPredicate;
        this.headerConfig = headerConfig;
        this.aliasConfig = aliasConfig;
        this.globalCellParser = cellParser;
        this.cellParsers = CollectionUtils.isEmpty(cellParsers) ? new ArrayList<>() : Collections.unmodifiableList(cellParsers);
        this.payload = payload;
        this.conversionService = conversionService;
        this.validators = validators;
        this.visitor = visitor;
        this.password = StringUtils.hasLength(password) ? password : null;
        this.excludeAllNullRow = excludeAllNullRow;
    }

    public static WalkerBuilder builder(Class<?> valueObjectType) {
        return new WalkerBuilder(valueObjectType);
    }

    public void walk() {
        Workbook wb = null;

        try {
            wb = createWorkbook();
            doWalk(wb);
        } catch (AbortException ignored) {
            // 强行中断
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    // nop
                }
            }

            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    // nop
                }
            }

            try {
                resource.getInputStream().close();
            } catch (IOException e) {
                // nop
            }

            visitor.afterProcessing(new VisitorContext(resource, wb), payload);
        }
    }

    private void doWalk(Workbook wb) throws AbortException {

        this.mergeAliasConfig(); // 反射别名覆盖配置定义别名
        this.initHeaderInfo(wb); // 初始化header
        this.betterExcludeRow(); // 微调excludeRowPredicate，使其可以跳过header行

        try {
            visitor.beforeProcessing(new VisitorContext(resource, wb), payload);
        } catch (Throwable e) {
            payload.incrErrorCount();
            if (ExitPolicy.ABORT == visitor.onError(new VisitorContext(resource, wb), payload, e)) {
                throw new AbortException();
            }
        }

        for (Sheet sh : wb) {
            if (!includeSheetPredicate.test(sh)) {
                continue;
            }

            HeaderInfo headerInfo = headerInfoMap.get(SheetUtils.getName(sh));
            if (headerInfo != null) {

                final String[] header = headerInfo.getData();

                for (Row row : sh) {
                    if (excludeRowPredicate.test(sh, row)) {
                        continue;
                    }

                    final String[] data = this.getRowData(row, header.length, headerInfo.getFirstCellIndex());
                    if (this.excludeAllNullRow && isAllNullElements(data)) {
                        continue;
                    }

                    final Object vo = InstanceUtils.newInstanceOrThrow(valueObjectType);

                    final BindingResult bindingResult = DataBinding.newInstance()
                            .valueObject(vo)
                            .conversionService(conversionService)
                            .validators(validators.toArray(new Validator[0]))
                            .data(NamedArray.builder()
                                    .addNames(header)
                                    .addObjects(data)
                                    .build())
                            .bind();

                    if (bindingResult.hasErrors()) {
                        try {
                            payload.incrInvalidDataCount();
                            visitor.onInvalidValueObject(new VisitorContext(resource, wb, sh, row), payload, vo, bindingResult);
                        } catch (Throwable e) {
                            payload.incrErrorCount();
                            if (ExitPolicy.ABORT == visitor.onError(new VisitorContext(resource, wb, sh, row), payload, e)) {
                                throw new AbortException();
                            }
                        }
                    } else {
                        try {
                            payload.incrSuccessCount();
                            visitor.onValidValueObject(new VisitorContext(resource, wb, sh, row), payload, vo);
                        } catch (Throwable e) {
                            payload.incrErrorCount();
                            if (ExitPolicy.ABORT == visitor.onError(new VisitorContext(resource, wb, sh, row), payload, e)) {
                                throw new AbortException();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isAllNullElements(String[] array) {
        for (String s : array) {
            if (s != null) return false;
        }
        return true;
    }

    private Workbook createWorkbook() {
        try {
            return doCreateWorkbook();
        } catch (UnsupportedFileFormatException e) {
            throw new IllegalArgumentException("unable to process: document is broken or encrypted");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("unable to process: document is encrypted");
        }
    }

    private Workbook doCreateWorkbook() throws IOException, GeneralSecurityException {

        if (password == null) {
            if (excelType == ExcelType.XSSF) {
                return new XSSFWorkbook(resource.getInputStream());
            } else {
                return new HSSFWorkbook(resource.getInputStream());
            }
        } else {
            fileSystem = new POIFSFileSystem(resource.getInputStream());
            if (excelType == ExcelType.XSSF) {
                EncryptionInfo info = new EncryptionInfo(fileSystem);
                Decryptor decryptor = Decryptor.getInstance(info);
                if (!decryptor.verifyPassword(password)) {
                    throw new IllegalArgumentException("unable to process: wrong password");
                }
                return new XSSFWorkbook(decryptor.getDataStream(fileSystem));
            } else {

                Biff8EncryptionKey.setCurrentUserPassword(password);
                return new HSSFWorkbook(fileSystem);
            }
        }
    }

    private void betterExcludeRow() {
        for (String sheetName : headerInfoMap.keySet()) {
            HeaderInfo info = headerInfoMap.get(sheetName);
            if (info != null) {
                this.excludeRowPredicate = RowPredicateFactories.any(
                        this.excludeRowPredicate,
                        RowPredicateFactories.indexInSet(info.getSheetIndex(), info.getRowIndex())
                );
            }
        }
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
                headerInfoMap.put(sheet.getSheetName(), headerInfo);
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
                headerInfoMap.put(sheetName, headerInfo);
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
                    headerInfoMap.put(sheet.getSheetName(), headerInfo);
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
                    headerInfoMap.put(sheetName, headerInfo);
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

    private void mergeAliasConfig() {
        this.aliasConfig.putAll(ValueObjectUtils.getAliases(this.valueObjectType));
    }

    private String[] getRowData(Row row, int headerSize, int firstCellIndex) {
        List<String> data = new ArrayList<>();
        for (int i = firstCellIndex; i < headerSize + firstCellIndex; i++) {
            Cell cell = row.getCell(i);
            data.add(getEffCellParser(SheetUtils.getIndex(cell.getSheet()), cell.getColumnIndex()).convert(cell));
        }
        return data.toArray(new String[0]);
    }

    public CellParser getEffCellParser(int sheetIndex, int columnIndex) {
        for (Tuple<Integer, Integer, CellParser> tuple : this.cellParsers) {
            if (tuple.getA() == sheetIndex && tuple.getB() == columnIndex) {
                return tuple.getC();
            }
        }
        return this.globalCellParser;
    }

}
