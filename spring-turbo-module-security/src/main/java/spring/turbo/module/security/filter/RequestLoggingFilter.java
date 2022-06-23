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
import spring.turbo.util.LogLevel;
import spring.turbo.util.Logger;
import spring.turbo.webmvc.AbstractServletFilter;
import spring.turbo.webmvc.HttpRequestSnapshot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static spring.turbo.util.StringPool.HYPHEN_X_80;

/**
 * @author 应卓
 * @see spring.turbo.module.security.FilterConfiguration
 * @since 1.0.0
 */
public class RequestLoggingFilter extends AbstractServletFilter {

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
        this.log = Optional.ofNullable(log)
                .orElse(DEFAULT_LOGGER);
    }

    @Override
    protected boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.log(HYPHEN_X_80);
            HttpRequestSnapshot.of(request)
                    .getLines()
                    .forEach(log::log);
            log.log(HYPHEN_X_80);
            return true;
        } catch (Throwable e) {
            return true;
        }
    }

}
