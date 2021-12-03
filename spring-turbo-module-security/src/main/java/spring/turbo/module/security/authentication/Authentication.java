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

import java.io.Serializable;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Mutable
public class Authentication extends AbstractAuthenticationToken implements org.springframework.security.core.Authentication, Serializable {

    private final UserDetails userDetails;

    public Authentication(@Nullable UserDetails userDetails) {
        super(
                Optional.ofNullable(userDetails)
                        .map(UserDetails::getAuthorities)
                        .orElse(null)
        );
        this.userDetails = userDetails;
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

}
