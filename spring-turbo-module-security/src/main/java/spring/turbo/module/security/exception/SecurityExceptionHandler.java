/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.RequestRejectedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 应卓
 * @see org.springframework.security.web.firewall.HttpFirewall
 * @since 1.2.3
 */
public interface SecurityExceptionHandler extends AuthenticationEntryPoint, AccessDeniedHandler, RequestRejectedHandler {

    @Override
    public default void handle(HttpServletRequest request, HttpServletResponse response, RequestRejectedException requestRejectedException) {
        request.setAttribute("SPRING_SECURITY_REJECTED_EXCEPTION", requestRejectedException);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public default void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, authException);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public default void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
