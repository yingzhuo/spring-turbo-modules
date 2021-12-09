/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author 应卓
 * @since 1.0.0
 */
public interface ExcelWalkerInterceptor {

    public static ExcelWalkerInterceptor getDefault() {
        return new ExcelWalkerInterceptor() {
        };
    }

    public default void onWorkbook(Workbook workbook, WalkingPayload payload) {
        // Noop
    }

    public default void onSheet(Workbook workbook, Sheet sheet, WalkingPayload payload) {
        // Noop
    }

    public default void onRow(Workbook workbook, Sheet sheet, Row row, WalkingPayload payload) {
        // Noop
    }

}
