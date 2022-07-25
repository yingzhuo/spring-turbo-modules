/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.webmvc.token.StringToken;
import spring.turbo.webmvc.token.Token;

/**
 * @author 应卓
 * @see Token
 * @see StringToken
 * @see UserDetails
 * @see spring.turbo.module.security.user.UserDetailsPlus
 * @since 1.1.3
 */
public final class NullTokenToUserConverter implements TokenToUserConverter {

    /**
     * 私有构造方法
     */
    private NullTokenToUserConverter() {
        super();
    }

    public static NullTokenToUserConverter getInstance() {
        return AsyncAvoid.INSTANCE;
    }

    @Override
    @Nullable
    public UserDetails convert(Token token) throws AuthenticationException {
        return null;
    }

    @Override
    @Nullable
    public UserDetails convert(String rawToken) throws AuthenticationException {
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class AsyncAvoid {
        private static final NullTokenToUserConverter INSTANCE = new NullTokenToUserConverter();
    }

}
