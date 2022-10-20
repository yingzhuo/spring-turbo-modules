/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsUtils;
import spring.turbo.webmvc.AbstractServletFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 应卓
 * @author 朱嘉杰
 * @see spring.turbo.module.security.FilterConfiguration
 * @since 1.0.0
 */
@Deprecated
public class CorsFilter extends AbstractServletFilter {

    /**
     * 构造方法
     */
    public CorsFilter() {
        super();
    }

    @Override
    protected boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, PATCH, OPTIONS, DELETE, TRACE, HEAD");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "X-Requested-With, Content-Type, Last-Modified, Accept, Origin");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        if (CorsUtils.isPreFlightRequest(request)) {
            response.setStatus(HttpStatus.OK.value());
            return false;
        }

        return true;
    }

}
