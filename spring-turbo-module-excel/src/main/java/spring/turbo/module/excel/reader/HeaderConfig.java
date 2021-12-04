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
import spring.turbo.module.excel.TextParser;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class HeaderConfig implements Serializable {

    private final Map<String, Pair<Integer, String[]>> merged = new HashMap<>();
    private final Map<String, Pair<Integer, String[]>> tableByName = new HashMap<>();
    private final Map<Integer, Pair<Integer, String[]>> tableByIndex = new HashMap<>();
    private boolean initialized = false;

    public static HeaderConfig newInstance() {
        return new HeaderConfig();
    }

    private HeaderConfig() {
        super();
    }

    public HeaderConfig add(String sheetName, int rowIndex) {
        Asserts.hasText(sheetName);
        Asserts.isTrue(rowIndex >= 0);
        tableByName.put(sheetName, Pair.of(rowIndex, null));
        return this;
    }

    public HeaderConfig add(int sheetIndex, int rowIndex) {
        Asserts.isTrue(sheetIndex >= 0);
        Asserts.isTrue(rowIndex >= 0);
        tableByIndex.put(sheetIndex, Pair.of(rowIndex, null));
        return this;
    }

    public void initialize(Workbook wb, TextParser textParser) {
        Asserts.notNull(wb);
        Asserts.notNull(textParser);

        if (initialized) {
            return;
        }

        for (String sheetName : tableByName.keySet()) {
            Pair<Integer, String[]> oldPair = tableByName.get(sheetName);
            String[] headerValue = getHeaderValue(wb, textParser, sheetName, oldPair.getA());
            if (headerValue == null) {
                String msg = String.format("cannot find header (sheetIndex = %s)", sheetName);
                throw new IllegalArgumentException(msg);
            }
            Pair<Integer, String[]> newPair = Pair.of(oldPair.getA(), headerValue);
            merged.put(sheetName, newPair);
        }

        for (int sheetIndex : tableByIndex.keySet()) {
            Pair<Integer, String[]> oldPair = tableByIndex.get(sheetIndex);
            String[] headerValue = getHeaderValue(wb, textParser, sheetIndex, oldPair.getA());
            if (headerValue == null) {
                String msg = String.format("cannot find header (sheetIndex = %d)", sheetIndex);
                throw new IllegalArgumentException(msg);
            }
            Pair<Integer, String[]> newPair = Pair.of(oldPair.getA(), headerValue);
            merged.put(wb.getSheetName(sheetIndex), newPair);
        }

        this.tableByName.clear();
        this.tableByIndex.clear();
        this.initialized = true;
    }

    private String[] getHeaderValue(Workbook wb, TextParser textParser, String sheetName, int headerRowIndex) {
        final Sheet sheet = wb.getSheet(sheetName);
        return doGetHeaderValue(textParser, headerRowIndex, sheet);
    }

    private String[] getHeaderValue(Workbook wb, TextParser textParser, int sheetIndex, int headerRowIndex) {
        Sheet sheet;
        try {
            sheet = wb.getSheetAt(sheetIndex);
        } catch (Exception e) {
            sheet = null;
        }
        return doGetHeaderValue(textParser, headerRowIndex, sheet);
    }

    private String[] doGetHeaderValue(TextParser textParser, int headerRowIndex, Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Row row = sheet.getRow(headerRowIndex);
        if (row == null) {
            return null;
        }
        List<String> headerValue = new LinkedList<>();
        for (Cell cell : row) {
            headerValue.add(textParser.toString(cell));
        }
        return headerValue.toArray(new String[0]);
    }

    public boolean isEmpty() {
        return this.initialized && merged.isEmpty();
    }

    public boolean canFindHeader(String sheetName) {
        Asserts.hasText(sheetName);
        return merged.containsKey(sheetName);
    }

    public boolean isContent(String sheetName, int currentRowIndex) {
        if (!canFindHeader(sheetName)) {
            System.out.println("2-1");
            return false;
        }
        Pair<Integer, String[]> pair = merged.get(sheetName);
        return currentRowIndex > pair.getA() && pair.getB() != null;
    }

    public String[] getHeader(String sheetName) {
        Asserts.hasText(sheetName);
        return merged.get(sheetName).getB();
    }

}
