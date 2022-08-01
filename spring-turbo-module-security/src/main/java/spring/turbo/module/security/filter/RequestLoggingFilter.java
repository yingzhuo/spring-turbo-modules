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
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.LogLevel;
import spring.turbo.util.Logger;
import spring.turbo.webmvc.SkippableFilter;
import spring.turbo.webmvc.function.RequestPredicateSet;

import javax.servlet.http.HttpServletRequest;

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

    private static final Logger DEFAULT_LOGGER = new Logger(RequestLoggingFilter.class, LogLevel.DEBUG);

    private final Logger log;
    private final RequestPredicateSet skipPredicates = new RequestPredicateSet();

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
        this.log = log != null ? log : DEFAULT_LOGGER;
        super.setIncludeHeaders(true);
        super.setIncludeQueryString(true);
        super.setIncludeClientInfo(true);
        super.setIncludePayload(false);
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
    protected boolean shouldLog(HttpServletRequest request) {
        if (!super.shouldLog(request)) {
            return false;
        }

        if (this.skipPredicates.isEmpty()) {
            return true;
        } else {
            return this.skipPredicates.noneMatches(request);
        }
    }

    @Override
    public final void addSkipPredicates(@Nullable RequestPredicateSet predicates) {
        CollectionUtils.nullSafeAddAll(skipPredicates, predicates);
    }

}
