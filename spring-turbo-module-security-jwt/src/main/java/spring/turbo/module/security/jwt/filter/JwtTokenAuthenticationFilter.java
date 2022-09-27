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
import spring.turbo.module.security.jwt.token.JwtTokenResolver;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.token.BearerTokenResolver;
import spring.turbo.webmvc.token.TokenResolver;

import javax.servlet.ServletException;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class JwtTokenAuthenticationFilter extends TokenAuthenticationFilter {

    /**
     * 构造方法
     */
    public JwtTokenAuthenticationFilter() {
        this.setTokenResolver(new BearerTokenResolver());
    }

    @Override
    public void setTokenResolver(TokenResolver resolver) {
        Asserts.notNull(resolver);

        if (resolver instanceof JwtTokenResolver) {
            super.setTokenResolver(resolver);
        } else {
            super.setTokenResolver(new JwtTokenResolver(resolver));
        }
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }

}
