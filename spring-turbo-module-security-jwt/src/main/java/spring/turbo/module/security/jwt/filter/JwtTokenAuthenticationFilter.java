/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.filter;

import spring.turbo.module.security.filter.TokenAuthenticationFilter;
import spring.turbo.webmvc.token.TokenResolver;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class JwtTokenAuthenticationFilter extends TokenAuthenticationFilter {

    private static final TokenResolver DEFAULT_TOKEN_RESOLVER;

    static {
        DEFAULT_TOKEN_RESOLVER =
                TokenResolver.builder()
                        .bearerToken()
                        .build();
    }

    public JwtTokenAuthenticationFilter() {
        super.setTokenResolver(DEFAULT_TOKEN_RESOLVER);
        super.setRememberMeServices(null);
        super.setAuthenticationEntryPoint(null);
    }

}
