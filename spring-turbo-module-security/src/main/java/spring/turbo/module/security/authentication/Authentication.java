package spring.turbo.module.security.authentication;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.token.Token;
import spring.turbo.module.security.util.AuthorityUtils;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

/**
 * 认证对象，本类是 {@link org.springframework.security.core.Authentication} 的实现
 *
 * @author 应卓
 * @see org.springframework.security.core.Authentication
 * @since 1.0.0
 */
public final class Authentication extends AbstractAuthenticationToken
        implements org.springframework.security.core.Authentication, Principal, Serializable {

    /**
     * 当前用户
     */
    @Nullable
    private final UserDetails userDetails;

    /**
     * 当前认证的令牌 (optional)
     *
     * @since 1.2.3
     */
    @Nullable
    private final Token token;

    /**
     * 默认构造方法
     */
    public Authentication() {
        this(null, null);
    }

    /**
     * 认证对象
     *
     * @param userDetails 用户信息
     */
    public Authentication(@Nullable UserDetails userDetails) {
        this(userDetails, null);
    }

    /**
     * 认证对象
     *
     * @param userDetails 用户信息
     * @param token       令牌
     */
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
        return Objects.requireNonNullElse(userDetails, Long.toString(System.identityHashCode(this)));
    }

    @NonNull
    @Override
    public Object getCredentials() {
        return Optional.ofNullable(userDetails).map(UserDetails::getPassword)
                .orElse(Long.toString(System.identityHashCode(this)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Authentication that = (Authentication) o;
        return Objects.equals(userDetails, that.userDetails) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userDetails, token);
    }

    @Nullable
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public UserDetails getRequiredUserDetails() {
        var ud = getUserDetails();
        Asserts.notNull(ud);
        return ud;
    }

    @Nullable
    public Token getToken() {
        return token;
    }

    public Token getRequiredToken() {
        var token = getToken();
        Asserts.notNull(token);
        return token;
    }

}
