/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.webmvc.HttpRequestSnapshot;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 应卓
 * @since 1.0.4
 */
@FunctionalInterface
public interface RequestDetailsBuilder {

    public static final RequestDetailsBuilder DEFAULT = request -> new WebAuthenticationDetailsSource().buildDetails(request);
    public static final RequestDetailsBuilder SNAPSHOT = request -> HttpRequestSnapshot.of(request).toString();
    public static final RequestDetailsBuilder DESCRIPTION = request -> new ServletWebRequest(request).getDescription(true);

    public Object getDetails(HttpServletRequest request);

}
