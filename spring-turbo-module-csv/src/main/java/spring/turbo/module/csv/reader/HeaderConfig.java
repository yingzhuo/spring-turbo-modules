/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.csv.reader;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.9
 */
public final class HeaderConfig implements Serializable {

    @Nullable
    private final String[] header;

    private final int index;

    public HeaderConfig(@NonNull String[] header) {
        Asserts.notNull(header);
        Asserts.noNullElements(header);

        this.index = -1;
        this.header = header;
    }

    public HeaderConfig(int index) {
        Asserts.isTrue(index >= 0);
        this.index = index;
        this.header = null;
    }

    public boolean isFixed() {
        return header != null;
    }

    @Nullable
    public String[] getHeader() {
        return header;
    }

    public int getIndex() {
        return index;
    }

}
