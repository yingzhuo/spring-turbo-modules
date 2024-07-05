package spring.turbo.module.security.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import spring.turbo.module.security.token.Token;

import javax.annotation.Nullable;

/**
 * 认证成功事件
 *
 * @author 应卓
 * @since 2.0.5
 */
public class AuthenticationSuccessEvent extends ApplicationEvent {

    @Nullable
    private final Token token;

    @Nullable
    private HttpServletRequest request;

    @Nullable
    private HttpServletResponse response;

    public AuthenticationSuccessEvent(Authentication source, @Nullable Token token) {
        super(source);
        this.token = token;
    }

    public Authentication getAuthentication() {
        return (Authentication) super.getSource();
    }

    @Nullable
    public Token getToken() {
        return token;
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
