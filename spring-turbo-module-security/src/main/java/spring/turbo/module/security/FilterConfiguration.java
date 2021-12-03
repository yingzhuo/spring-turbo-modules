/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import spring.turbo.bean.Factory;

import javax.servlet.Filter;

/**
 * @param <T> FilterType
 * @author 应卓
 * @since 1.0.0
 */
@FunctionalInterface
public interface FilterConfiguration<T extends Filter> extends Factory<T> {

    @Nullable
    @Override
    public T create();

    @NonNull
    public default Class<? extends Filter> positionInChain() {
        return BasicAuthenticationFilter.class;
    }

    @NonNull
    public default BeforeOrAfter beforeOrAfter() {
        return BeforeOrAfter.AFTER;
    }

    public static enum BeforeOrAfter {
        BEFORE, AFTER;
    }

}
