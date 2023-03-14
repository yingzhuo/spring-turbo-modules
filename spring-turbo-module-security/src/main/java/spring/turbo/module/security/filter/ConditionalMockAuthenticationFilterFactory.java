/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import jakarta.servlet.Filter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import spring.turbo.module.security.FilterConfiguration;

/**
 * @author 应卓
 * @since 2.1.1
 */
public interface ConditionalMockAuthenticationFilterFactory extends FilterConfiguration<ConditionalMockAuthenticationFilter> {

    @Override
    public default Position position() {
        return Position.AFTER;
    }

    @Override
    public default Class<? extends Filter> positionInChain() {
        return LogoutFilter.class;
    }

}
