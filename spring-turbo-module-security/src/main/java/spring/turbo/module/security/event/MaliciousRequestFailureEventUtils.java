/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.event;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.RequestAuthentication;
import spring.turbo.util.Asserts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author 应卓
 * @see MaliciousRequestFailureEvent
 * @since 1.0.4
 */
public final class MaliciousRequestFailureEventUtils {

    private MaliciousRequestFailureEventUtils() {
        super();
    }

    @Nullable
    public static ServletWebRequest getServletWebRequest(@NonNull MaliciousRequestFailureEvent event) {
        Asserts.notNull(event);
        final Authentication authentication = event.getAuthentication();
        if (authentication instanceof RequestAuthentication) {
            return ((RequestAuthentication) authentication).getServletWebRequest();
        } else {
            return null;
        }
    }

    @Nullable
    public static HttpServletRequest getHttpServletRequest(@NonNull MaliciousRequestFailureEvent event) {
        return Optional.ofNullable(getServletWebRequest(event)).map(ServletWebRequest::getRequest).orElse(null);
    }

    @Nullable
    public static HttpServletResponse getHttpServletResponse(@NonNull MaliciousRequestFailureEvent event) {
        return Optional.ofNullable(getServletWebRequest(event)).map(ServletWebRequest::getResponse).orElse(null);
    }

}