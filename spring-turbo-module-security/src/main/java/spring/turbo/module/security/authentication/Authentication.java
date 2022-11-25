/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.lang.Mutable;
import spring.turbo.module.security.util.AuthorityUtils;
import spring.turbo.webmvc.token.Token;

import java.util.Optional;

/**
 * 认证对象，本类是 {@link org.springframework.security.core.Authentication} 的实现
 *
 * @author 应卓
 * @see org.springframework.security.core.Authentication
 * @since 1.0.0
 */
@Mutable
public class Authentication extends AbstractAuthenticationToken implements org.springframework.security.core.Authentication {

    /**
     * 当前用户
     */
    @Nullable
    private final UserDetails userDetails;

    /**
     * 当前认证的令牌 (Optional)
     *
     * @since 1.2.3
     */
    @Nullable
    private final Token token;

    public Authentication() {
        this(null, null);
    }

    public Authentication(@Nullable UserDetails userDetails, @Nullable Token token) {
        super(AuthorityUtils.getAuthorities(userDetails));
        this.userDetails = userDetails;
        this.token = token;
        super.setAuthenticated(userDetails != null);
        super.setDetails(null);
    }

    @NonNull
    @Override
    public Object getPrincipal() {
        if (userDetails != null) {
            return userDetails;
        } else {
            return Long.toString(System.identityHashCode(this));
        }
    }

    @NonNull
    @Override
    public Object getCredentials() {
        return Optional.ofNullable(userDetails)
                .map(UserDetails::getPassword)
                .orElse(Long.toString(System.identityHashCode(this)));
    }

    @Nullable
    public final UserDetails getUserDetails() {
        return userDetails;
    }

    @Nullable
    public Token getToken() {
        return token;
    }

}
