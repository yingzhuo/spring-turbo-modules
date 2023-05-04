/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import org.springframework.lang.Nullable;
import spring.turbo.bean.Named;
import spring.turbo.util.Asserts;
import spring.turbo.util.EnumUtils;

import java.io.Serializable;

/**
 * @author 应卓
 *
 * @since 2.0.1
 */
public interface Item extends Named, Serializable {

    public static Item of(String string) {
        return new SimpleItem(string);
    }

    public default String getName() {
        return toString();
    }

    @Nullable
    public default <E extends Enum<E>> E toEnum(Class<E> enumClass) {
        return EnumUtils.getEnumIgnoreCase(enumClass, getName());
    }

    public default <E extends Enum<E>> E toNonNullEnum(Class<E> enumClass) {
        final E en = toEnum(enumClass);
        Asserts.notNull(en);
        return en;
    }

}
