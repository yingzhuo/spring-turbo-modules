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
import org.springframework.lang.Nullable;
import spring.turbo.util.StringPool;

/**
 * @author 应卓
 * @since 1.1.0
 */
public class SmartCellParser extends DefaultCellParser {

    public SmartCellParser() {
        super();
    }

    public SmartCellParser(String dateFormatPattern) {
        super(dateFormatPattern);
    }

    @Nullable
    @Override
    public String convert(@Nullable Cell cell) {
        String s = super.convert(cell);

        if (s == null) return null;

        // "-" 等同于 null
        if (StringPool.HYPHEN.equals(s)) {
            return null;
        }

        // 去除字串中的逗号
        if (s.contains(",")) {
            s = s.replaceAll(StringPool.COMMA, StringPool.EMPTY);
        }

        // 去除字符串结尾的百分号
        if (s.endsWith("%")) {
            s = s.substring(0, s.length() - 1);
        }

        return s;
    }

}
