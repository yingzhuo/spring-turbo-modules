package spring.turbo.module.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.firewall.RequestRejectedException;

import java.io.IOException;

/**
 * @author 应卓
 * @since 1.2.3
 */
public class SecurityExceptionHandlerImpl implements SecurityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       RequestRejectedException requestRejectedException) throws IOException, ServletException {
        log.debug("handle \"{}\" exception", requestRejectedException.getClass().getName());
        request.setAttribute("SPRING_SECURITY_REJECTED_EXCEPTION", requestRejectedException);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException, ServletException {
        log.debug("handle \"{}\" exception", authenticationException.getClass().getName());
        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, authenticationException);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.debug("handle \"{}\" exception", accessDeniedException.getClass().getName());
        request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
