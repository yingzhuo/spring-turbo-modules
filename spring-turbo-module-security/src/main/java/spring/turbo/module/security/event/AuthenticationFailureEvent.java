package spring.turbo.module.security.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;


/**
 * 认证失败事件
 *
 * @author 应卓
 * @since 2.0.5
 */
public class AuthenticationFailureEvent extends ApplicationEvent {

    @Nullable
    private HttpServletRequest request;

    @Nullable
    private HttpServletResponse response;

    public AuthenticationFailureEvent(AuthenticationException source) {
        super(source);
    }

    public AuthenticationException getAuthenticationException() {
        return (AuthenticationException) super.getSource();
    }

    @Nullable
    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(@Nullable HttpServletRequest request) {
        this.request = request;
    }

    @Nullable
    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(@Nullable HttpServletResponse response) {
        this.response = response;
    }

}
