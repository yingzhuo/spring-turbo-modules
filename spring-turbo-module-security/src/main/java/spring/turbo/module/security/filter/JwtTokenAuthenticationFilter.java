/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.security.web.util.matcher.RequestMatcher;
import spring.turbo.module.security.authentication.MapTokenToUserConverter;
import spring.turbo.module.security.token.BearerTokenResolver;

/**
 * @author 应卓
 * @see MapTokenToUserConverter
 * @see TokenAuthenticationFilter
 * @see RequestMatcher
 * @since 1.0.1
 */
public final class JwtTokenAuthenticationFilter extends TokenAuthenticationFilter {

    /**
     * 默认构造方法
     */
    public JwtTokenAuthenticationFilter() {
        setTokenResolver(new BearerTokenResolver());
    }

}
