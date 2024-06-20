/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import spring.turbo.util.Asserts;

/**
 * Servlet相关工具
 *
 * @author 应卓
 * @since 1.0.0
 */
public final class ServletUtils {

    /**
     * 私有构造方法
     */
    private ServletUtils() {
        super();
    }

    @Nullable
    public static HttpServletRequest getRequest() {
        try {
            var attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpServletRequest getRequiredRequest() {
        var request = getRequest();
        Asserts.notNull(request);
        return request;
    }

    public static HttpServletRequest getUnwrappedRequest() {
        var request = getRequiredRequest();
        while (request instanceof HttpServletRequestWrapper wrapper) {
            request = (HttpServletRequest) (wrapper).getRequest();
        }
        return request;
    }

    @Nullable
    public static HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes();
            return attributes.getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpServletResponse getRequiredResponse() {
        final HttpServletResponse response = getResponse();
        Asserts.notNull(response);
        return response;
    }

    public static HttpServletResponse getUnwrappedResponse() {
        var response = getRequiredResponse();
        while (response instanceof HttpServletResponseWrapper wrapper) {
            response = (HttpServletResponse) (wrapper).getResponse();
        }
        return response;
    }

    public static HttpSession getSession() {
        return getSession(true);
    }

    public static HttpSession getSession(boolean create) {
        return getRequiredRequest().getSession(create);
    }

    public static String getSessionId() {
        return getSession().getId();
    }

    public static ServletContext getServletContext() {
        return getRequiredRequest().getServletContext();
    }

}
