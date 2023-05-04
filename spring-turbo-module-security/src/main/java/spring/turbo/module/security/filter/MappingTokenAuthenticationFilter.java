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
import spring.turbo.module.security.user.UserDetailsPlus;
import spring.turbo.util.Asserts;
import spring.turbo.util.RandomStringUtils;

/**
 * @author 应卓
 *
 * @see MapTokenToUserConverter
 * @see TokenAuthenticationFilter
 * @see TokenAuthenticationFilterFactory
 * @see RequestMatcher
 * @see #newInstance()
 *
 * @since 2.2.2
 */
public final class MappingTokenAuthenticationFilter extends TokenAuthenticationFilter {

    /**
     * 私有构造方法
     */
    private MappingTokenAuthenticationFilter() {
        super.setTokenResolver(new BearerTokenResolver());
        super.setTokenToUserConverter(new MapTokenToUserConverter());
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static MappingTokenAuthenticationFilter newInstance() {
        return new MappingTokenAuthenticationFilter();
    }

    public MappingTokenAuthenticationFilter addUserDetails(String rawToken, String... authorities) {
        var uuid = RandomStringUtils.randomUUID(true);
        return addUserDetails(rawToken,
                UserDetailsPlus.builder().id(uuid).username(uuid).password(uuid).authorities(authorities).build());
    }

    public MappingTokenAuthenticationFilter addUserDetails(String rawToken, UserDetails userDetails) {
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
