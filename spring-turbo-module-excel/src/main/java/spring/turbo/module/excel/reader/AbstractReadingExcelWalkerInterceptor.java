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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Payload;
import spring.turbo.module.excel.ExcelWalkerInterceptor;
import spring.turbo.module.excel.TextParser;
import spring.turbo.util.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
public abstract class AbstractReadingExcelWalkerInterceptor implements ExcelWalkerInterceptor {

    private final Headers headers;
    private final TextParser textParser;

    public AbstractReadingExcelWalkerInterceptor(Headers headers) {
        this(headers, null);
    }

    public AbstractReadingExcelWalkerInterceptor(@NonNull Headers headers, @Nullable TextParser textParser) {
        Asserts.notNull(headers);
        this.headers = headers;
        this.textParser = textParser != null ? textParser : TextParser.getDefault();
    }

    @Override
    public final void onSheet(Workbook workbook, Sheet sheet, Payload payload) {
        if (headers.isEmpty()) {
            return;
        }

        Map<String, Pair<Integer, String[]>> table = headers.getConfigTable();
        for (String name : table.keySet()) {
            if (sheet.getSheetName().equals(name)) {
                Pair<Integer, String[]> pair = table.get(name);
                Optional<String[]> headerOption = getHeader(sheet, pair.getA());
                if (headerOption.isPresent()) {
                    Pair<Integer, String[]> newPair = Pair.of(pair.getA(), headerOption.get());
                    table.put(name, newPair);
                } else {
                    String msg = String.format("cannot parse header for sheet %s", name);
                    throw new IllegalArgumentException(msg);
                }
                break;
            }
        }

        doOnSheet(workbook, sheet, payload);
    }

    protected void doOnSheet(Workbook workbook, Sheet sheet, Payload payload) {
        // 可以被覆盖
    }

    @Override
    public final void onRow(Workbook workbook, Sheet sheet, Row row, Payload payload) {
        if (headers.isEmpty()) {
            return;
        }
        final String currentSheetName = sheet.getSheetName();
        final int currentRowIndex = row.getRowNum();

        Map<String, Pair<Integer, String[]>> table = headers.getConfigTable();
        String[] header = table.get(currentSheetName).getB();
        if (!table.containsKey(currentSheetName)) {
            return;
        }

        if (currentRowIndex <= table.get(currentSheetName).getA()) {
            return;
        }

        String[] data = getRowData(row, header).orElse(null);

        doOnRow(currentSheetName, currentRowIndex, header, data);
    }

    protected void doOnRow(String sheetName, int rowIndex, String[] header, String[] rowData) {
        // 可以被覆盖
    }

    private Optional<String[]> getHeader(Sheet sheet, int rowNumber) {
        Row row = sheet.getRow(rowNumber);
        List<String> header = new ArrayList<>();
        for (Cell cell : row) {
            header.add(textParser.toString(cell));
        }
        return Optional.of(header.toArray(new String[0]));
    }

    private Optional<String[]> getRowData(Row row, String[] header) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < header.length; i++) {
            String h = header[i];
            if (h == null) {
                data.add(null);
            } else {
                Cell cell = row.getCell(i);
                data.add(textParser.toString(cell));
            }
        }
        return Optional.of(data.toArray(new String[0]));
    }

}
