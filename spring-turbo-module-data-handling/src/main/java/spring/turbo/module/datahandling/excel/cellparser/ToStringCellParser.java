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

import java.util.Optional;

/**
 * 这个东西功能太弱，不推荐使用
 *
 * @author 应卓
 * @since 1.0.1
 */
@Deprecated
public class ToStringCellParser implements GlobalCellParser {

    /**
     * 私有构造方法
     */
    private ToStringCellParser() {
        super();
    }

    public static ToStringCellParser getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public String convert(@Nullable Cell cell) {
        return Optional.ofNullable(cell).map(Cell::toString).orElse(null);
    }

    static class SyncAvoid {
        private static final ToStringCellParser INSTANCE = new ToStringCellParser();
    }

}
