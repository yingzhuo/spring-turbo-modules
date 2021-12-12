/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class SheetUtils {

    private SheetUtils() {
        super();
    }

    public static String getName(Sheet sheet) {
        Asserts.notNull(sheet);
        return sheet.getSheetName();
    }

    public static int getIndex(Sheet sheet) {
        Asserts.notNull(sheet);
        return sheet.getWorkbook().getSheetIndex(sheet);
    }

    public static boolean isHidden(Sheet sheet) {
        Asserts.notNull(sheet);
        return sheet.getWorkbook().isSheetHidden(getIndex(sheet));
    }

    public static boolean isVisitable(Sheet sheet) {
        Asserts.notNull(sheet);
        return !isHidden(sheet);
    }

    public static Workbook getParent(Sheet sheet) {
        Asserts.notNull(sheet);
        return sheet.getWorkbook();
    }

}
