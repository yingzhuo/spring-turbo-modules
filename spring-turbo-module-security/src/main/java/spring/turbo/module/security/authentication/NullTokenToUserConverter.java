package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.token.StringToken;
import spring.turbo.module.security.token.Token;

/**
 * @author 应卓
 * @see #getInstance()
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
    }

    /**
     * 获取单例实例
     *
     * @return 实例
     */
    public static NullTokenToUserConverter getInstance() {
        return AsyncAvoid.INSTANCE;
    }

    @Override
    @Nullable
    public UserDetails convert(Token token) throws AuthenticationException {
        return null;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    private static class AsyncAvoid {
        private static final NullTokenToUserConverter INSTANCE = new NullTokenToUserConverter();
    }

}
