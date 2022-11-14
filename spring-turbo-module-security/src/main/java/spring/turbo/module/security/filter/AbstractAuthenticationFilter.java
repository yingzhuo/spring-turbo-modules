/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.turbo.module.security.authentication.RequestDetailsProvider;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.token.TokenResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 应卓
 * @see BasicAuthenticationFilter
 * @see TokenAuthenticationFilter
 * @since 1.2.3
 */
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

    @Nullable
    protected TokenResolver tokenResolver;

    @Nullable
    protected RememberMeServices rememberMeServices;

    @Nullable
    protected RequestDetailsProvider requestDetailsProvider = RequestDetailsProvider.DEFAULT;

    @Nullable
    protected AuthenticationEventPublisher authenticationEventPublisher;

    @Nullable
    protected AuthenticationEntryPoint authenticationEntryPoint;

    protected final boolean authenticationIsRequired() {
        final Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }
        return (existingAuth instanceof AnonymousAuthenticationToken);
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        // nop
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        // nop
    }

    public final void setTokenResolver(TokenResolver tokenResolver) {
        Asserts.notNull(tokenResolver);
        this.tokenResolver = tokenResolver;
    }

    public final void setRememberMeServices(@Nullable RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public final void setRequestDetailsProvider(@Nullable RequestDetailsProvider requestDetailsProvider) {
        this.requestDetailsProvider = requestDetailsProvider;
    }

    public final void setAuthenticationEventPublisher(@Nullable AuthenticationEventPublisher authenticationEventPublisher) {
        this.authenticationEventPublisher = authenticationEventPublisher;
    }

    public final void setAuthenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

}
