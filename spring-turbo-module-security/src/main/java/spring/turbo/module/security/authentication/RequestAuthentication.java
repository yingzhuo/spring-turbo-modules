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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.lang.Mutable;
import spring.turbo.module.security.util.AuthorityUtils;

import java.util.Collection;

/**
 * 空认证对象，仅仅用于占位和携带请求
 *
 * @author 应卓
 * @since 1.0.4
 */
@Mutable
public final class RequestAuthentication implements Authentication {

    @Nullable
    public final ServletWebRequest servletWebRequest;

    private RequestAuthentication(@Nullable ServletWebRequest servletWebRequest) {
        this.servletWebRequest = servletWebRequest;
    }

    public static RequestAuthentication newInstance(@Nullable ServletWebRequest servletWebRequest) {
        return new RequestAuthentication(servletWebRequest);
    }

    @Nullable
    public ServletWebRequest getServletWebRequest() {
        return servletWebRequest;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.noAuthorities();
    }

    @Nullable
    @Override
    public Object getCredentials() {
        return null;
    }

    @Nullable
    @Override
    public Object getDetails() {
        return null;
    }

    @Nullable
    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("cannot set authenticated");
    }

    @Override
    public String getName() {
        return "no-name";
    }

}
