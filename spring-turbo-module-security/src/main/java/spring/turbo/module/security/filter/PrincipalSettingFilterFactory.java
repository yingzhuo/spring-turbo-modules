/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import spring.turbo.module.security.FilterConfiguration;

import javax.servlet.Filter;

/**
 * @author 应卓
 * @since 1.1.2
 */
public interface PrincipalSettingFilterFactory extends FilterConfiguration<PrincipalSettingFilter> {

    @Override
    default Position position() {
        return Position.AFTER;
    }

    @Override
    public default Class<? extends Filter> positionInChain() {
        return AnonymousAuthenticationFilter.class;
    }

}
