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

import java.util.function.Function;

/**
 * 从cell中解析出文本
 *
 * @author 应卓
 * @see GlobalCellParser
 * @since 1.0.0
 */
@FunctionalInterface
public interface CellParser extends Function<Cell, String> {

    @Nullable
    public String convert(@Nullable Cell cell);

    @Nullable
    @Override
    public default String apply(@Nullable Cell cell) {
        return convert(cell);
    }

}
