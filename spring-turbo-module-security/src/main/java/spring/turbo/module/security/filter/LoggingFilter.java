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
 * @since 1.0.0
 */
public class LoggingFilter extends AbstractServletFilter {

    private static final Logger DEFAULT_LOGGER = new Logger(LoggingFilter.class, LogLevel.DEBUG);

    private final Logger log;

    // since 1.0.1
    public LoggingFilter() {
        this(null);
    }

    public LoggingFilter(@Nullable Logger logger) {
        this.log = Optional.ofNullable(logger).orElse(DEFAULT_LOGGER);
    }

    @Override
    protected boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        log.log(HYPHEN_X_80);
        HttpRequestSnapshot.of(request)
                .getLines()
                .forEach(log::log);
        log.log(HYPHEN_X_80);
        return true;
    }

}
