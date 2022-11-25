/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication.details;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.RemoteAddressUtils;
import spring.turbo.webmvc.token.Token;

import java.util.Date;

/**
 * @author 应卓
 * @since 1.2.3
 */
public class AuthenticationDetailsImpl implements AuthenticationDetails {

    private final Date authenticatedTime;

    private final String path;

    @Nullable
    private final Token authenticatedToken;

    @Nullable
    private final String clientIp;

    public AuthenticationDetailsImpl(HttpServletRequest request) {
        this(request, null);
    }

    public AuthenticationDetailsImpl(HttpServletRequest request, @Nullable Token token) {
        Asserts.notNull(request);
        Asserts.notNull(token);
        this.authenticatedTime = new Date();
        this.authenticatedToken = token;
        this.path = request.getRequestURI();
        this.clientIp = RemoteAddressUtils.getIpAddress(request);
    }

    @Override
    public Date getAuthenticatedTime() {
        return this.authenticatedTime;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    @Nullable
    public Token getAuthenticatedToken() {
        return this.authenticatedToken;
    }

    @Override
    @Nullable
    public String getClientId() {
        return this.clientIp;
    }

}
