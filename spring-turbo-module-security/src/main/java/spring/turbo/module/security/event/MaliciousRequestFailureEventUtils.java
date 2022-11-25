/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.RequestAuthentication;
import spring.turbo.util.Asserts;

import java.util.Optional;

/**
 * @author 应卓
 * @see MaliciousRequestFailureEvent
 * @see spring.turbo.module.security.exception.MaliciousRequestException
 * @since 1.0.4
 */
public final class MaliciousRequestFailureEventUtils {

    /**
     * 私有构造方法
     */
    private MaliciousRequestFailureEventUtils() {
        super();
    }

    @Nullable
    public static ServletWebRequest getServletWebRequest(MaliciousRequestFailureEvent event) {
        Asserts.notNull(event);
        final Authentication authentication = event.getAuthentication();
        if (authentication instanceof RequestAuthentication) {
            return ((RequestAuthentication) authentication).getServletWebRequest();
        } else {
            return null;
        }
    }

    @Nullable
    public static HttpServletRequest getRequest(MaliciousRequestFailureEvent event) {
        return Optional.ofNullable(getServletWebRequest(event)).map(ServletWebRequest::getRequest).orElse(null);
    }

    @Nullable
    public static HttpServletResponse getResponse(MaliciousRequestFailureEvent event) {
        return Optional.ofNullable(getServletWebRequest(event)).map(ServletWebRequest::getResponse).orElse(null);
    }

    public static HttpServletRequest getRequiredRequest(MaliciousRequestFailureEvent event) {
        final HttpServletRequest request = getRequest(event);
        Asserts.notNull(request);
        return request;
    }

    public static HttpServletResponse getRequiredResponse(MaliciousRequestFailureEvent event) {
        final HttpServletResponse response = getResponse(event);
        Asserts.notNull(response);
        return response;
    }

}
