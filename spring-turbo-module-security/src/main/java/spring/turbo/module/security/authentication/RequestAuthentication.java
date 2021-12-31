/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.lang.Mutable;

/**
 * 空认证对象，仅仅用于占位和携带请求
 *
 * @author 应卓
 * @since 1.0.4
 */
@Mutable
public final class RequestAuthentication extends Authentication {

    public final ServletWebRequest servletWebRequest;

    private RequestAuthentication(@Nullable ServletWebRequest servletWebRequest) {
        super(null);
        super.setAuthenticated(false);
        this.servletWebRequest = servletWebRequest;
    }

    public static RequestAuthentication newInstance(@Nullable ServletWebRequest servletWebRequest) {
        return new RequestAuthentication(servletWebRequest);
    }

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Nullable
    public ServletWebRequest getServletWebRequest() {
        return servletWebRequest;
    }

}
