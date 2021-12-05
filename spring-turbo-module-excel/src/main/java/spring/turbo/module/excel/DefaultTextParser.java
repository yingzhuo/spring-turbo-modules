/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class DefaultTextParser implements TextParser {

    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    private final DateFormat dateFormat;

    public DefaultTextParser() {
        this(DEFAULT_DATE_FORMAT_PATTERN);
    }

    public DefaultTextParser(String dateFormatPattern) {
        this.dateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    @Override
    public String toString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
            case FORMULA:
            case BOOLEAN:
                return cell.toString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());
                }
                return cell.toString();
            case BLANK:
            case ERROR:
                return null;
            default:
                // nop
                return null;
        }
    }

}
