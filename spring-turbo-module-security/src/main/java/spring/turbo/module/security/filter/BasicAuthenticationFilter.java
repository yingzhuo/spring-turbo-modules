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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.Authentication;
import spring.turbo.module.security.authentication.NullUserDetailsFinder;
import spring.turbo.module.security.authentication.UserDetailsFinder;
import spring.turbo.module.security.event.AuthenticationFailureEvent;
import spring.turbo.module.security.event.AuthenticationSuccessEvent;
import spring.turbo.module.security.token.BasicToken;
import spring.turbo.module.security.token.BasicTokenResolver;

import java.io.IOException;

/**
 * HttpBasic认证过滤器
 *
 * <p>
 * Spring Security 已经提供了此功能。但是笔者认为其严重设计过度，不是特别习惯使用 {@link org.springframework.security.web.authentication.www.BasicAuthenticationFilter}。
 * 故此，设计了一个简化版本的过滤器。
 * </p>
 *
 * @author 应卓
 * @see TokenAuthenticationFilter
 * @see spring.turbo.module.security.filter.factory.BasicAuthenticationFilterFactoryBean
 * @since 1.2.3
 */
@Deprecated(since = "3.3.2")
public class BasicAuthenticationFilter extends AbstractAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthenticationFilter.class);

    @Nullable
    private UserDetailsFinder userDetailsFinder = NullUserDetailsFinder.getInstance();

    /**
     * 构造方法
     */
    public BasicAuthenticationFilter() {
        super.setTokenResolver(new BasicTokenResolver());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 其他过滤器已经做完了认证工作则跳过
        if (!super.authenticationIsRequired()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var token = tokenResolver.resolve(new ServletWebRequest(request, response)).orElse(null);

            if (this.tokenBlacklistManager != null && token != null) {
                this.tokenBlacklistManager.verify(token);
            }

            if (!(token instanceof BasicToken basicToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String username = basicToken.getUsername();
            final String password = basicToken.getPassword();

            final UserDetails user = userDetailsFinder.loadUserByUsernameAndPassword(username, password);
            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            final Authentication auth = new Authentication(user, token);
            auth.setAuthenticated(true);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);

            if (this.rememberMeServices != null) {
                rememberMeServices.loginSuccess(request, response, auth);
            }

            onSuccessfulAuthentication(request, response, auth);

            if (this.applicationEventPublisher != null) {
                final var event = new AuthenticationSuccessEvent(auth, token);
                event.setRequest(request);
                event.setResponse(response);
                this.applicationEventPublisher.publishEvent(event);
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

            if (this.applicationEventPublisher != null) {
                final var event = new AuthenticationFailureEvent(e);
                event.setRequest(request);
                event.setResponse(response);
                this.applicationEventPublisher.publishEvent(event);
            }

            if (authenticationEntryPoint != null) {
                authenticationEntryPoint.commence(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setUserDetailsFinder(UserDetailsFinder userDetailsFinder) {
        this.userDetailsFinder = userDetailsFinder;
    }

}
