/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.RequestAuthentication;
import spring.turbo.module.security.authentication.RequestDetailsBuilder;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.AbstractServletFilter;
import spring.turbo.webmvc.token.Token;
import spring.turbo.webmvc.token.TokenResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 * @see spring.turbo.module.security.FilterConfiguration
 * @since 1.0.0
 */
public class TokenAuthenticationFilter extends AbstractServletFilter {

    private TokenResolver tokenResolver; // non-null
    private TokenToUserConverter tokenToUserConverter; // non-null
    private RememberMeServices rememberMeServices; // nullable
    private AuthenticationEventPublisher authenticationEventPublisher;  // nullable
    private AuthenticationEntryPoint authenticationEntryPoint;  // nullable
    private RequestDetailsBuilder requestDetailsBuilder = RequestDetailsBuilder.SNAPSHOT; // nullable

    public TokenAuthenticationFilter() {
        super();
    }

    @Override
    protected boolean doFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!authenticationIsRequired()) {
            return true;
        }

        try {
            final Token token = tokenResolver.resolve(new ServletWebRequest(request)).orElse(null);
            if (token == null) {
                return true;
            }

            UserDetails user = tokenToUserConverter.convert(token);
            if (user == null) {
                return true;
            }

            final spring.turbo.module.security.authentication.Authentication auth
                    = new spring.turbo.module.security.authentication.Authentication(user);
            auth.setAuthenticated(true);

            if (requestDetailsBuilder != null) {
                auth.setDetails(requestDetailsBuilder.getDetails(request));
            }

            SecurityContextHolder.getContext().setAuthentication(auth);

            if (this.rememberMeServices != null) {
                rememberMeServices.loginSuccess(request, response, auth);
            }

            onSuccessfulAuthentication(request, response, auth);

            if (this.authenticationEventPublisher != null) {
                authenticationEventPublisher.publishAuthenticationSuccess(auth);
            }

        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();

            if (rememberMeServices != null) {
                rememberMeServices.loginFail(request, response);
            }

            onUnsuccessfulAuthentication(request, response, e);

            if (this.authenticationEventPublisher != null) {
                // 注意第二参数如果传null会导致spring-security不能顺利new出event对象
                // 因此拉一个垫背的
                authenticationEventPublisher.publishAuthenticationFailure(e, RequestAuthentication.newInstance(new ServletWebRequest(request, response)));
            }

            if (authenticationEntryPoint != null) {
                authenticationEntryPoint.commence(request, response, e);
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        Asserts.notNull(tokenResolver);
        Asserts.notNull(tokenToUserConverter);
    }

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

    public void setTokenResolver(@NonNull TokenResolver tokenResolver) {
        Asserts.notNull(tokenResolver);
        this.tokenResolver = tokenResolver;
    }

    public void setTokenToUserConverter(@NonNull TokenToUserConverter converter) {
        Asserts.notNull(converter);
        this.tokenToUserConverter = converter;
    }

    public void setRememberMeServices(@Nullable RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setAuthenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setAuthenticationEventPublisher(@Nullable AuthenticationEventPublisher authenticationEventPublisher) {
        this.authenticationEventPublisher = authenticationEventPublisher;
    }

    public void setRequestDetailsBuilder(@Nullable RequestDetailsBuilder requestDetailsBuilder) {
        this.requestDetailsBuilder = requestDetailsBuilder;
    }
}
