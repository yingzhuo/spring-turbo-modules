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
import spring.turbo.util.StringPool;
import spring.turbo.webmvc.AbstractServletFilter;
import spring.turbo.webmvc.HttpRequestSnapshot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 应卓
 * @see RequestLoggingFilter
 * @since 1.1.3
 */
public class HumanReadableRequestLoggingFilter extends AbstractServletFilter {

    private final Logger log;

    public HumanReadableRequestLoggingFilter() {
        this(null);
    }

    public HumanReadableRequestLoggingFilter(@Nullable Logger log) {
        this.log = log != null ? log : new Logger(HumanReadableRequestLoggingFilter.class, LogLevel.DEBUG);
    }

    @Override
    protected boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        try {
            doLog(request);
        } catch (Exception ignored) {
        }
        return true;
    }

    private void doLog(HttpServletRequest request) {
        if (!log.isEnabled()) {
            return;
        }

        log.log(StringPool.HYPHEN_X_80);
        HttpRequestSnapshot.of(request)
                .getLines()
                .forEach(log::log);
        log.log(StringPool.HYPHEN_X_80);
    }

}
