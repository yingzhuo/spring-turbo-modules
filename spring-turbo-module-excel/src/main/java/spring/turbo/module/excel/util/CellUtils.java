/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import spring.turbo.bean.Tuple;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.util.Asserts;

import java.util.Objects;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class CellUtils {

    private static final CellParser PARSER = new DefaultCellParser();

    /**
     * 私有构造方法
     */
    private CellUtils() {
        super();
    }

    public static Tuple<Workbook, Sheet, Row> getParent(Cell cell) {
        Asserts.notNull(cell);
        Row row = cell.getRow();
        Sheet sheet = row.getSheet();
        Workbook workbook = sheet.getWorkbook();
        return Tuple.of(workbook, sheet, row);
    }

    public static String toString(Cell cell) {
        Asserts.notNull(cell);
        return Objects.requireNonNull(PARSER.convert(cell));
    }

}
