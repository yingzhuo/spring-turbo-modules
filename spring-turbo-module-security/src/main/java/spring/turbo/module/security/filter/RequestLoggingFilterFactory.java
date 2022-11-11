/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.lang.NonNull;
import spring.turbo.module.security.FilterConfiguration;

import javax.servlet.Filter;

/**
 * @author 应卓
 * @see HumanReadableRequestLoggingFilter
 * @see RequestLoggingFilter
 * @since 1.0.0
 */
@FunctionalInterface
public interface RequestLoggingFilterFactory extends FilterConfiguration<Filter> {

    @Override
    public default Filter get() {
        return new HumanReadableRequestLoggingFilter();
    }

    @NonNull
    @Override
    public default Position position() {
        return Position.BEFORE;
    }

}
