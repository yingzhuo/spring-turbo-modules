/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.excel.cellparser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.lang.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author 应卓
 * @see org.springframework.format.annotation.DateTimeFormat.ISO#DATE_TIME
 * @see spring.turbo.format.DateTimeFormat
 * @see SmartCellParser (推荐)
 * @since 1.0.0
 */
public class DefaultCellParser implements GlobalCellParser {

    public static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // ISO DateTime

    private final DateFormat dateFormat;

    public DefaultCellParser() {
        this(DEFAULT_DATE_FORMAT_PATTERN);
    }

    public DefaultCellParser(String dateFormatPattern) {
        this.dateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    @Nullable
    @Override
    public String convert(@Nullable Cell cell) {
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
                return null;
        }
    }

}
