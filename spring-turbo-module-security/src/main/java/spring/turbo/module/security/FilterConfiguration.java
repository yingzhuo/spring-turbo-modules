/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security;

import jakarta.servlet.Filter;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import spring.turbo.bean.Factory;

/**
 * Filter配置单元
 * <p>
 * 本类用来配置SpringSecurity扩展过滤器，必须把此类的实现加入 {@link org.springframework.context.ApplicationContext}。
 *
 * @param <T> FilterType
 * @author 应卓
 * @see spring.turbo.module.security.filter.TokenAuthenticationFilter
 * @see spring.turbo.module.security.filter.RequestLoggingFilter
 * @see org.springframework.security.web.SecurityFilterChain
 * @since 1.0.0
 */
@FunctionalInterface
public interface FilterConfiguration<T extends Filter> extends Factory<T> {

    public default boolean isEnabled() {
        return true;
    }

    @Nullable
    @Override
    public T create();

    public default Class<? extends Filter> positionInChain() {
        return BasicAuthenticationFilter.class;
    }

    public default Position position() {
        return Position.AFTER;
    }

    public enum Position {
        BEFORE, AFTER, REPLACE
    }

}
