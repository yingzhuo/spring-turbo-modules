package spring.turbo.module.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.turbo.module.security.authentication.RequestDetailsProvider;
import spring.turbo.module.security.token.TokenResolver;
import spring.turbo.module.security.token.blacklist.TokenBlacklistManager;

/**
 * @author 应卓
 * @see BasicAuthenticationFilter
 * @see TokenAuthenticationFilter
 * @see RequestMatcher
 * @since 1.2.3
 */
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

    @Nullable
    protected TokenResolver tokenResolver;

    @Nullable
    protected TokenBlacklistManager tokenBlacklistManager;

    @Nullable
    protected RememberMeServices rememberMeServices;

    @Nullable
    protected RequestDetailsProvider requestDetailsProvider;

    @Nullable
    protected AuthenticationEntryPoint authenticationEntryPoint;

    @Nullable
    protected ApplicationEventPublisher applicationEventPublisher;

    protected final boolean authenticationIsRequired() {
        final Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }
        return (existingAuth instanceof AnonymousAuthenticationToken);
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              Authentication authResult) {
        // nop
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException failed) {
        // nop
    }

    public final void setTokenResolver(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    public void setTokenBlacklistManager(@Nullable TokenBlacklistManager tokenBlacklistManager) {
        this.tokenBlacklistManager = tokenBlacklistManager;
    }

    public final void setRememberMeServices(@Nullable RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public final void setRequestDetailsProvider(@Nullable RequestDetailsProvider requestDetailsProvider) {
        this.requestDetailsProvider = requestDetailsProvider;
    }

    public final void setAuthenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setApplicationEventPublisher(@Nullable ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

}
