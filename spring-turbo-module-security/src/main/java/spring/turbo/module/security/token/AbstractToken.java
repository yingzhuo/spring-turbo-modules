/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.token;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import spring.turbo.module.security.util.AuthorityUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author 应卓
 *
 * @since 3.1.1
 */
public abstract class AbstractToken implements Token {

    @Nullable
    private Collection<? extends GrantedAuthority> authorities;

    @Nullable
    private Object details;

    private Object principal;
    private Object credentials;

    private boolean authenticated = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Objects.requireNonNullElse(authorities, AuthorityUtils.noAuthorities());
    }

    @Override
    public void setAuthorities(@Nullable Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    @Override
    public void setDetails(@Nullable Object details) {
        this.details = details;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

}
