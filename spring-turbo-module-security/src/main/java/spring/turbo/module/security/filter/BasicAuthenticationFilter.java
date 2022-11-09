/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.NullUserDetailsFinder;
import spring.turbo.module.security.authentication.RequestAuthentication;
import spring.turbo.module.security.authentication.RequestDetailsProvider;
import spring.turbo.module.security.authentication.UserDetailsFinder;
import spring.turbo.webmvc.token.BasicToken;
import spring.turbo.webmvc.token.NullTokenResolver;
import spring.turbo.webmvc.token.Token;
import spring.turbo.webmvc.token.TokenResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 * @since 1.2.3
 */
public class BasicAuthenticationFilter extends AbstractAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    @Nullable
    private TokenResolver tokenResolver;

    @Nullable
    private UserDetailsFinder userDetailsFinder;

    @Nullable
    private RequestDetailsProvider requestDetailsProvider;

    @Nullable
    private RememberMeServices rememberMeServices;

    @Nullable
    private AuthenticationEventPublisher authenticationEventPublisher;

    @Nullable
    private AuthenticationEntryPoint authenticationEntryPoint;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!super.isAuthenticationIsRequired()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final Token token = tokenResolver.resolve(new ServletWebRequest(request, response)).orElse(null);
            if (!(token instanceof BasicToken)) {
                filterChain.doFilter(request, response);
                return;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("token resolved");
                    log.debug("token value (string): {}", token.asString());
                    log.debug("token username: {}", ((BasicToken) token).getUsername());
                    log.debug("token password: {}", ((BasicToken) token).getPassword());
                }
            }

            final BasicToken basicToken = (BasicToken) token;
            String username = basicToken.getUsername();
            String password = basicToken.getPassword();

            final UserDetails user = userDetailsFinder.find(username, password);
            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            final spring.turbo.module.security.authentication.Authentication auth
                    = new spring.turbo.module.security.authentication.Authentication(user, token);
            auth.setAuthenticated(true);

            if (requestDetailsProvider != null) {
                auth.setDetails(requestDetailsProvider.getDetails(request));
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
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }

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
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        if (this.tokenResolver == null) {
            this.tokenResolver = NullTokenResolver.getInstance();
        }

        if (this.userDetailsFinder == null) {
            this.userDetailsFinder = NullUserDetailsFinder.getInstance();
        }
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

    public void setUserDetailsFinder(UserDetailsFinder userDetailsFinder) {
        this.userDetailsFinder = userDetailsFinder;
    }

    public void setRequestDetailsProvider(@Nullable RequestDetailsProvider requestDetailsProvider) {
        this.requestDetailsProvider = requestDetailsProvider;
    }

    public void setRememberMeServices(@Nullable RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setAuthenticationEventPublisher(@Nullable AuthenticationEventPublisher authenticationEventPublisher) {
        this.authenticationEventPublisher = authenticationEventPublisher;
    }

    public void setAuthenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

}
