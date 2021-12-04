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
import spring.turbo.bean.Payload;
import spring.turbo.module.excel.ExcelWalkerInterceptor;
import spring.turbo.module.excel.TextParser;
import spring.turbo.util.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
public abstract class AbstractReadingExcelWalkerInterceptor implements ExcelWalkerInterceptor {

    private final HeaderConfig headerConfig;
    private final TextParser textParser;

    public AbstractReadingExcelWalkerInterceptor(HeaderConfig headerConfig) {
        this(headerConfig, null);
    }

    public AbstractReadingExcelWalkerInterceptor(@NonNull HeaderConfig headerConfig, @Nullable TextParser textParser) {
        Asserts.notNull(headerConfig);
        this.headerConfig = headerConfig;
        this.textParser = textParser != null ? textParser : TextParser.getDefault();
    }

    @Override
    public final void onWorkbook(Workbook workbook, Payload payload) {
        headerConfig.initialize(workbook, textParser);
        this.doOnWorkbook(workbook, payload);
    }

    protected void doOnWorkbook(Workbook workbook, Payload payload) {
        // Noop
    }

    @Override
    public final void onRow(Workbook workbook, Sheet sheet, Row row, Payload payload) {
        if (headerConfig.isEmpty()) {
            return;
        }

        final String currentSheetName = sheet.getSheetName();
        final int currentRowIndex = row.getRowNum();

        if (!headerConfig.isContent(currentSheetName, currentRowIndex)) {
            return;
        }

        String[] header = headerConfig.getHeader(currentSheetName);
        String[] data = getRowData(row, header).orElse(null);

        if (data == null) {
            return;
        }

        doOnRow(currentSheetName, currentRowIndex, header, data);
    }

    protected void doOnRow(String sheetName, int rowIndex, String[] header, String[] rowData) {
        // 可以被覆盖
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
