/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.excel.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import spring.turbo.bean.Pair;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class RowUtils {

    /**
     * 私有构造方法
     */
    private RowUtils() {
        super();
    }

    public static Pair<Workbook, Sheet> getParent(Row row) {
        Asserts.notNull(row);
        Sheet sheet = row.getSheet();
        Workbook workbook = sheet.getWorkbook();
        return Pair.ofNonNull(workbook, sheet);
    }

    public static int getIndex(Row row) {
        Asserts.notNull(row);
        return row.getRowNum();
    }

    public static boolean isZeroHeight(Row row) {
        Asserts.notNull(row);
        return row.getZeroHeight();
    }

    public static boolean isNotZeroHeight(Row row) {
        return !isZeroHeight(row);
    }

    public static boolean isFormatted(Row row) {
        Asserts.notNull(row);
        return row.isFormatted();
    }

    public static boolean isNotFormatted(Row row) {
        return !isFormatted(row);
    }

}
