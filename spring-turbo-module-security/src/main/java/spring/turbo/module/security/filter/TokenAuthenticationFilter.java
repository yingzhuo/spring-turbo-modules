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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.AbstractServletFilter;
import spring.turbo.webmvc.HttpRequestSnapshot;
import spring.turbo.webmvc.token.Token;
import spring.turbo.webmvc.token.TokenResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class TokenAuthenticationFilter extends AbstractServletFilter {

    private TokenResolver tokenResolver;
    private TokenToUserConverter tokenToUserConverter;
    private RememberMeServices rememberMeServices;
    private AuthenticationEntryPoint authenticationEntryPoint;

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

            spring.turbo.module.security.authentication.Authentication auth
                    = new spring.turbo.module.security.authentication.Authentication(user);
            auth.setAuthenticated(true);
            auth.setDetails(HttpRequestSnapshot.of(request).toString());

            SecurityContextHolder.getContext().setAuthentication(auth);
            rememberMeServices.loginSuccess(request, response, auth);
            onSuccessfulAuthentication(request, response, auth);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();

            if (rememberMeServices != null) {
                rememberMeServices.loginFail(request, response);
            }

            onUnsuccessfulAuthentication(request, response, e);

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

        if (rememberMeServices == null) {
            rememberMeServices = new NullRememberMeServices();
        }
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

    public void setTokenResolver(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    public void setTokenToAuthenticationConverter(TokenToUserConverter tokenToUserConverter) {
        this.tokenToUserConverter = tokenToUserConverter;
    }

    public void setRememberMeServices(@Nullable RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setAuthenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

}
