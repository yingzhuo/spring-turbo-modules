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
import org.springframework.web.filter.OncePerRequestFilter;
import spring.turbo.util.LogLevel;
import spring.turbo.util.Logger;
import spring.turbo.util.StringPool;
import spring.turbo.webmvc.HttpRequestSnapshot;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 应卓
 * @see RequestLoggingFilter
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 * @see org.springframework.web.filter.AbstractRequestLoggingFilter
 * @since 1.1.3
 */
public class HumanReadableRequestLoggingFilter extends OncePerRequestFilter {

    private final Logger log;

    /**
     * 构造方法
     */
    public HumanReadableRequestLoggingFilter() {
        this(null);
    }

    /**
     * 构造方法
     *
     * @param log 日志记录器
     */
    public HumanReadableRequestLoggingFilter(@Nullable Logger log) {
        this.log = log != null ? log : new Logger(HumanReadableRequestLoggingFilter.class, LogLevel.DEBUG);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doLog(request);
        } catch (Exception ignored) {
            // NOP
        }
        filterChain.doFilter(request, response);
    }

    private void doLog(HttpServletRequest request) {
        if (log.isEnabled()) {
            log.log(StringPool.HYPHEN_X_80);
            HttpRequestSnapshot.of(request)
                    .getLines()
                    .forEach(log::log);
            log.log(StringPool.HYPHEN_X_80);
        }
    }

}
