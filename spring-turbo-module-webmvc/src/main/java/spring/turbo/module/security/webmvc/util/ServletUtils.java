/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.webmvc.util;

import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class ServletUtils {

    private ServletUtils() {
        super();
    }

    @NonNull
    public static HttpServletRequest getRequest() {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    @NonNull
    public static HttpServletRequest getUnwrappedRequest() {
        HttpServletRequest request = getRequest();

        while (request instanceof HttpServletRequestWrapper) {
            request = (HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest();
        }

        return request;
    }

    @NonNull
    public static HttpServletResponse getResponse() {
        final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getResponse();
    }

    @NonNull
    public static HttpServletResponse getUnwrappedResponse() {
        HttpServletResponse response = getResponse();

        while (response instanceof HttpServletResponseWrapper) {
            response = (HttpServletResponse) ((HttpServletResponseWrapper) response).getResponse();
        }

        return response;
    }

    @NonNull
    public static HttpSession getSession() {
        return getSession(true);
    }

    public static HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    @NonNull
    public static String getSessionId() {
        return getSession().getId();
    }

    public static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

}
