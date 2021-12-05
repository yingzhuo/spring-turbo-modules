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
import spring.turbo.bean.Pair;
import spring.turbo.bean.Payload;
import spring.turbo.bean.Tuple;
import spring.turbo.module.excel.ExcelWalkerInterceptor;
import spring.turbo.module.excel.TextParser;
import spring.turbo.module.excel.func.RowPredicate;
import spring.turbo.module.excel.func.RowPredicateFactories;
import spring.turbo.util.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.0
 */
public abstract class AbstractReadingExcelWalkerInterceptor implements ExcelWalkerInterceptor {

    private final HeaderConfig headerConfig;
    private AliasConfig aliasConfig = AliasConfig.newInstance();
    private TextParser textParser = TextParser.getDefault();
    private RowPredicate excludeRowPredicate = RowPredicateFactories.alwaysFalse();
    private final Map<String, HeaderInfo> headerInfoMap = new HashMap<>();

    public AbstractReadingExcelWalkerInterceptor(HeaderConfig headerConfig) {
        Asserts.notNull(headerConfig);
        this.headerConfig = headerConfig;
    }

    @Override
    public final void onWorkbook(Workbook workbook, Payload payload) {

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
                headerInfo.setData(mergeWithAlias(data));
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
                headerInfo.setData(mergeWithAlias(data));
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
                    int firstCellNum = row.getFirstCellNum();
                    final List<String> data = new ArrayList<>();
                    for (Cell cell : row) {
                        data.add(textParser.toString(cell));
                    }
                    HeaderInfo headerInfo = new HeaderInfo();
                    headerInfo.setSheetName(sheet.getSheetName());
                    headerInfo.setSheetIndex(sheetIndex);
                    headerInfo.setRowIndex(headerRowIndex);
                    headerInfo.setFirstCellIndex(firstCellNum);
                    headerInfo.setData(mergeWithAlias(data.toArray(new String[0])));
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
                    int firstCellNum = row.getFirstCellNum();
                    final List<String> data = new ArrayList<>();
                    for (Cell cell : row) {
                        data.add(textParser.toString(cell));
                    }
                    HeaderInfo headerInfo = new HeaderInfo();
                    headerInfo.setSheetName(sheetName);
                    headerInfo.setSheetIndex(workbook.getSheetIndex(sheet));
                    headerInfo.setRowIndex(headerRowIndex);
                    headerInfo.setFirstCellIndex(firstCellNum);
                    headerInfo.setData(mergeWithAlias(data.toArray(new String[0])));
                    headerInfoMap.put(sheetName, headerInfo);
                }
            }
        }

        this.doOnWorkbook(workbook, payload);
    }

    protected void doOnWorkbook(Workbook workbook, Payload payload) {
        // Noop
    }

    private String[] mergeWithAlias(String[] header) {
        if (this.aliasConfig == null) {
            return header;
        }

        for (int i = 0; i < header.length; i++) {
            header[i] = aliasConfig.getOrDefault(header[i], header[i]);
        }
        return header;
    }

    @Override
    public final void onRow(Workbook workbook, Sheet sheet, Row row, Payload payload) {
        if (excludeRowPredicate.test(sheet, row)) {
            return;
        }

        String sheetName = sheet.getSheetName();
        int rowIndex = row.getRowNum();

        if (headerInfoMap.containsKey(sheetName)) {

            HeaderInfo info = headerInfoMap.get(sheetName);

            if (rowIndex == info.getRowIndex()) {
                return;
            }

            String[] data = getRowData(row, info.getData().length, info.getFirstCellIndex());
            doOnRow(sheetName, row.getRowNum(), info.getData(), data);
        }
    }

    protected void doOnRow(String sheetName, int rowIndex, String[] header, String[] rowData) {
        // 可以被覆盖
    }

    private String[] getRowData(Row row, int headerSize, int firstCellIndex) {
        List<String> data = new ArrayList<>();
        for (int i = firstCellIndex; i < headerSize + firstCellIndex; i++) {
            Cell cell = row.getCell(i);
            data.add(textParser.toString(cell));
        }
        return data.toArray(new String[0]);
    }

    public void setAliasConfig(AliasConfig aliasConfig) {
        Asserts.notNull(aliasConfig);
        this.aliasConfig = aliasConfig;
    }

    public void setTextParser(TextParser textParser) {
        Asserts.notNull(textParser);
        this.textParser = textParser;
    }

    public void setExcludeRowPredicate(RowPredicate excludeRowPredicate) {
        Asserts.notNull(excludeRowPredicate);
        this.excludeRowPredicate = excludeRowPredicate;
    }

}
