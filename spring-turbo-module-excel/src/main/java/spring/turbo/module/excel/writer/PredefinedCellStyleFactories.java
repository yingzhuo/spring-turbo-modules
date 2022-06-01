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

/**
 * @author 应卓
 * @since 1.1.0
 */
public final class PredefinedCellStyleFactories {

    /**
     * 私有构造方法
     */
    private PredefinedCellStyleFactories() {
        super();
    }

    public static CellStyle createCellStyleForHeader(Workbook workbook) {
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

    public static CellStyle createCellStyleForCommonData(Workbook workbook) {
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

    public static CellStyle createCellStyleForDateTypeData(Workbook workbook) {
        final Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(false);

        final CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss")
        );
        return style;
    }

}
