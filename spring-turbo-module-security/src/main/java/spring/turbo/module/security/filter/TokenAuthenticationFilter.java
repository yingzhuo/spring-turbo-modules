/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.Authentication;
import spring.turbo.module.security.authentication.RequestAuthentication;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.token.BearerTokenResolver;
import spring.turbo.webmvc.token.StringToken;
import spring.turbo.webmvc.token.Token;

import java.io.IOException;

/**
 * 基于令牌的认证过滤器
 *
 * @author 应卓
 * @see spring.turbo.module.security.FilterConfiguration
 * @since 1.0.0
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Nullable
    private TokenToUserConverter tokenToUserConverter;

    /**
     * 构造方法
     */
    public TokenAuthenticationFilter() {
        super();
        super.setTokenResolver(new BearerTokenResolver());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!authenticationIsRequired()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (skipRequestMatcher != null && skipRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Asserts.notNull(this.tokenResolver);
        Asserts.notNull(this.tokenToUserConverter);

        try {
            final Token token = tokenResolver.resolve(new ServletWebRequest(request)).orElse(null);
            if (token == null) {
                log.debug("token cannot be resolved");
                filterChain.doFilter(request, response);
                return;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("token resolved");
                    if (token instanceof StringToken) {
                        log.debug("token value (string): {}", token.asString());
                    }
                }
            }

            final UserDetails user = tokenToUserConverter.convert(token);
            if (user == null) {
                log.debug("cannot convert token to UserDetails instance");
                filterChain.doFilter(request, response);
                return;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("UserDetails converted. (username: {})", user.getUsername());
                }
            }

            final Authentication auth = new Authentication(user, token);
            auth.setAuthenticated(true);

            if (requestDetailsProvider != null) {
                auth.setDetails(requestDetailsProvider.getDetails(request, token));
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

    public void setTokenToUserConverter(TokenToUserConverter converter) {
        Asserts.notNull(converter);
        this.tokenToUserConverter = converter;
    }

}
