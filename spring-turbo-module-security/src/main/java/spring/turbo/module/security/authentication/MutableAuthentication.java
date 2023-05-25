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
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.util.AuthorityUtils;
import spring.turbo.util.Asserts;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 *
 * @since 3.1.1
 */
public class MutableAuthentication implements Authentication {

    @Nullable
    private Collection<? extends GrantedAuthority> authorities;

    private Object principal;

    private Object credentials;

    @Nullable
    private Object details;

    private boolean authenticated = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Objects.requireNonNullElse(this.authorities, AuthorityUtils.noAuthorities());
    }

    public void setAuthorities(@Nullable Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(Object principal) {
        Asserts.notNull(principal, "principal is null");
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    public void setCredentials(Object credentials) {
        Asserts.notNull(credentials, "credentials is null");
        this.credentials = credentials;
    }

    @Nullable
    @Override
    public Object getDetails() {
        return this.details;
    }

    public void setDetails(@Nullable Object details) {
        this.details = details;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public final String getName() {
        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }
        if (this.getPrincipal() instanceof AuthenticatedPrincipal) {
            return ((AuthenticatedPrincipal) this.getPrincipal()).getName();
        }
        if (this.getPrincipal() instanceof Principal) {
            return ((Principal) this.getPrincipal()).getName();
        }
        return (this.getPrincipal() == null) ? EMPTY : this.getPrincipal().toString();
    }

}
