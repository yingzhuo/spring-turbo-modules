/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.writer;

import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;
import spring.turbo.util.Asserts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.7
 */
abstract class AbstractAnnotationDrivenExcelView extends AbstractXlsxStreamingView {

    private final WorkbookMetadata workbookMetadata;

    public AbstractAnnotationDrivenExcelView(WorkbookMetadata workbookMetadata) {
        Asserts.notNull(workbookMetadata);
        this.workbookMetadata = workbookMetadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {

        // 应答头处理
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        final String filename = workbookMetadata.getFilename();
        if (filename != null) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        }

        for (SheetMetadata<?> sheetMetadata : workbookMetadata.getSheetMetadata().toArray(new SheetMetadata[0])) {
            final Collection<?> data = sheetMetadata.getDataCollectionSupplier()
                    .apply(model, sheetMetadata.getValueObjectType());
            final String sheetName = sheetMetadata.getSheetName();
            final Sheet sheet = workbook.createSheet(sheetName);

            buildExcelSheet(workbook, sheet, sheetMetadata, data, new ServletWebRequest(request, response));
        }
    }

    protected abstract void buildExcelSheet(Workbook workbook, Sheet sheet, SheetMetadata<?> sheetMetadata, Collection<?> data, ServletWebRequest servletWebRequest);

    @NonNull
    protected final CellStyle createDefaultCellStyleForHeader(Workbook workbook) {
        final Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);

        final CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    @NonNull
    protected final CellStyle createDefaultCellStyleForData(Workbook workbook) {
        final Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(false);

        final CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
