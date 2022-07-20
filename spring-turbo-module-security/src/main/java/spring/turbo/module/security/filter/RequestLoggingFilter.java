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
import spring.turbo.util.LogLevel;
import spring.turbo.util.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 应卓
 * @see spring.turbo.module.security.FilterConfiguration
 * @since 1.0.0
 */
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    private static final Logger DEFAULT_LOGGER = new Logger(RequestLoggingFilter.class, LogLevel.DEBUG);

    private final Logger log;

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

}
