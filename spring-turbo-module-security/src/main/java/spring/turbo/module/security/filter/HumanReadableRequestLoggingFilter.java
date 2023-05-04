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
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.LogLevel;
import spring.turbo.util.Logger;
import spring.turbo.util.StringUtils;
import spring.turbo.webmvc.HttpRequestSnapshot;

import java.io.IOException;
import java.util.Objects;

import static spring.turbo.util.StringPool.HYPHEN_X_80;
import static spring.turbo.util.StringPool.LF;

/**
 * @author 应卓
 *
 * @see RequestLoggingFilter
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 * @see org.springframework.web.filter.AbstractRequestLoggingFilter
 * @see RequestMatcher
 *
 * @since 1.1.3
 */
public class HumanReadableRequestLoggingFilter extends OncePerRequestFilter implements SkippableFilter {

    private final Logger log;

    @Nullable
    private RequestMatcher skipRequestMatcher;

    /**
     * 构造方法
     */
    public HumanReadableRequestLoggingFilter() {
        this(null);
    }

    /**
     * 构造方法
     *
     * @param log
     *            日志记录器
     */
    public HumanReadableRequestLoggingFilter(@Nullable Logger log) {
        this.log = Objects.requireNonNullElseGet(log,
                () -> new Logger(HumanReadableRequestLoggingFilter.class, LogLevel.DEBUG));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (skipRequestMatcher != null && skipRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            doLog(request);
        } catch (Exception ignored) {
            // NoOp
        }
        filterChain.doFilter(request, response);
    }

    private void doLog(HttpServletRequest request) {
        if (log.isEnabled()) {
            final var lines = HttpRequestSnapshot.of(request).getLinesList();
            if (CollectionUtils.isNotEmpty(lines)) {
                final var text = LF + HYPHEN_X_80 + LF + StringUtils.nullSafeJoin(lines, LF) + LF + HYPHEN_X_80;
                log.log(text);
            }
        }
    }

    @Override
    public void setSkipRequestMatcher(@Nullable RequestMatcher skipRequestMatcher) {
        this.skipRequestMatcher = skipRequestMatcher;
    }

}
