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
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;
import spring.turbo.module.security.SkippableFilter;
import spring.turbo.util.LogLevel;
import spring.turbo.util.Logger;

import java.io.IOException;

/**
 * @author 应卓
 * @see spring.turbo.module.security.FilterConfiguration
 * @see AbstractRequestLoggingFilter
 * @see CommonsRequestLoggingFilter
 * @see ServletContextRequestLoggingFilter
 * @see HumanReadableRequestLoggingFilter
 * @since 1.0.0
 */
public class RequestLoggingFilter extends AbstractRequestLoggingFilter implements SkippableFilter {

    private final Logger log;

    @Nullable
    private RequestMatcher skipRequestMatcher;

    /**
     * 构造方法
     */
    public RequestLoggingFilter() {
        this(null);
    }

    /**
     * 构造方法
     *
     * @param log 日志记录器
     */
    public RequestLoggingFilter(@Nullable Logger log) {
        this.log = log != null ? log : new Logger(RequestLoggingFilter.class, LogLevel.DEBUG);
        super.setIncludeHeaders(true);
        super.setIncludeQueryString(true);
        super.setIncludeClientInfo(true);
        super.setIncludePayload(false);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (skipRequestMatcher != null && skipRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        super.doFilterInternal(request, response, filterChain);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.log(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.log(message);
    }

    @Override
    public void setSkipRequestMatcher(@Nullable RequestMatcher skipRequestMatcher) {
        this.skipRequestMatcher = skipRequestMatcher;
    }

}
