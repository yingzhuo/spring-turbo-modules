/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.filter;

import org.springframework.lang.NonNull;
import spring.turbo.module.security.filter.TokenAuthenticationFilter;
import spring.turbo.module.security.jwt.token.JwtTokenResolver;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.token.TokenResolver;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class JwtTokenAuthenticationFilter extends TokenAuthenticationFilter {

    public JwtTokenAuthenticationFilter() {
        super.setTokenResolver(new JwtTokenResolver());
    }

    @Override
    public void setTokenResolver(@NonNull TokenResolver tokenResolver) {
        Asserts.notNull(tokenResolver);
        super.setTokenResolver(new JwtTokenResolver(tokenResolver));
    }

}
