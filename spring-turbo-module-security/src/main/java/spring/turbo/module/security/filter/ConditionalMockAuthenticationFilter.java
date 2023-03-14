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
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.turbo.module.security.util.RequestMatcherFactories;

import java.io.IOException;

/**
 * @author 应卓
 * @since 2.1.1
 */
public class ConditionalMockAuthenticationFilter extends OncePerRequestFilter implements SkippableFilter {

    @Nullable
    private Condition condition = (request, response) -> null;

    @Nullable
    private RequestMatcher skipRequestMatcher = RequestMatcherFactories.alwaysFalse();

    /**
     * 默认构造方法
     */
    public ConditionalMockAuthenticationFilter() {
        super();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (skipRequestMatcher != null && skipRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = condition != null ? condition.match(request, response) : null;
        if (auth != null) {
            auth.setAuthenticated(true);
            SecurityContextHolder.getContext()
                    .setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void setSkipRequestMatcher(@Nullable RequestMatcher skipRequestMatcher) {
        this.skipRequestMatcher = skipRequestMatcher;
    }

    public void setCondition(@Nullable Condition condition) {
        this.condition = condition;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @FunctionalInterface
    public static interface Condition {
        @Nullable
        public Authentication match(HttpServletRequest request, HttpServletResponse response);
    }

}
