package spring.turbo.module.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.RequestRejectedHandler;

import java.io.IOException;

/**
 * @author 应卓
 * @see AuthenticationEntryPoint
 * @see AccessDeniedHandler
 * @see RequestRejectedHandler
 * @see org.springframework.security.web.firewall.HttpFirewall
 * @since 1.2.3
 */
public interface SecurityExceptionHandler
        extends AuthenticationEntryPoint, AccessDeniedHandler, RequestRejectedHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       RequestRejectedException requestRejectedException) throws IOException, ServletException;

    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException, ServletException;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException;

}
