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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * 清理用过滤器
 *
 * @author 应卓
 * @since 3.3.1
 */
public class CleanupFilter extends OncePerRequestFilter {

    private final BiConsumer<HttpServletRequest, HttpServletResponse> logic;
    private final boolean ignoreExceptions;

    public CleanupFilter(BiConsumer<HttpServletRequest, HttpServletResponse> logic) {
        this(logic, true);
    }

    public CleanupFilter(BiConsumer<HttpServletRequest, HttpServletResponse> logic, boolean ignoreExceptions) {
        this.logic = logic;
        this.ignoreExceptions = ignoreExceptions;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        try {
            logic.accept(request, response);
        } catch (Exception e) {
            if (!ignoreExceptions) {
                throw e;
            }
        }
    }

}
