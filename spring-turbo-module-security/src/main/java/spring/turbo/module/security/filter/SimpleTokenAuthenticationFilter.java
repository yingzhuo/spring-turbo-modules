/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import spring.turbo.module.security.authentication.MapTokenToUserConverter;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.module.security.token.BearerTokenResolver;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @see MapTokenToUserConverter
 * @see TokenAuthenticationFilter
 * @see TokenAuthenticationFilterFactory
 * @see RequestMatcher
 * @since 2.2.2
 */
public final class SimpleTokenAuthenticationFilter extends TokenAuthenticationFilter {

    /**
     * 默认构造方法
     */
    public SimpleTokenAuthenticationFilter() {
        super.setTokenResolver(new BearerTokenResolver());
        super.setTokenToUserConverter(new MapTokenToUserConverter());
    }

    public SimpleTokenAuthenticationFilter addUserDetails(String rawToken, UserDetails userDetails) {
        var converter = (MapTokenToUserConverter) getTokenToUserConverter();
        Asserts.notNull(converter);
        converter.add(rawToken, userDetails);
        return this;
    }

    @Override
    public void setTokenToUserConverter(TokenToUserConverter converter) {
        throw new UnsupportedOperationException();
    }

}
