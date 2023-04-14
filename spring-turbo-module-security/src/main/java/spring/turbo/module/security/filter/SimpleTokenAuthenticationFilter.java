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
import spring.turbo.module.security.authentication.MapTokenToUserConverter;
import spring.turbo.module.security.token.BearerTokenResolver;

/**
 * @author 应卓
 * @see MapTokenToUserConverter
 * @since 2.2.2
 */
public class SimpleTokenAuthenticationFilter extends TokenAuthenticationFilter {

    public SimpleTokenAuthenticationFilter() {
        super();
        super.setTokenResolver(new BearerTokenResolver());
        super.setTokenToUserConverter(new MapTokenToUserConverter());
    }

    public void addUserDetails(String rawToken, UserDetails userDetails) {
        if (getTokenToUserConverter() instanceof MapTokenToUserConverter converter) {
            converter.add(rawToken, userDetails);
        }
    }

}
