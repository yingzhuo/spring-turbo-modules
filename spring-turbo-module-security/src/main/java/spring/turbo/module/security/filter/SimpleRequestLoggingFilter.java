/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

/**
 * @author 应卓
 *
 * @see spring.turbo.module.security.FilterConfiguration
 * @see AbstractRequestLoggingFilter
 * @see CommonsRequestLoggingFilter
 * @see ServletContextRequestLoggingFilter
 * @see HumanReadableRequestLoggingFilter
 *
 * @since 1.0.0
 */
public class SimpleRequestLoggingFilter extends AbstractRequestLoggingFilter implements SkippableFilter {

    @Nullable
    private RequestMatcher skipRequestMatcher;

    /**
     * 默认构造方法
     */
    public SimpleRequestLoggingFilter() {
        setIncludeClientInfo(true);
        setIncludeHeaders(true);
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(100);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

    @Override
    public void setSkipRequestMatcher(RequestMatcher skipRequestMatcher) {
        this.skipRequestMatcher = skipRequestMatcher;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return skipRequestMatcher != null && !skipRequestMatcher.matches(request);
    }

}
