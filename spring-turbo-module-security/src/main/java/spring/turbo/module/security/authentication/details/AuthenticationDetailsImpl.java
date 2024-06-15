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
import spring.turbo.module.security.token.Token;
import spring.turbo.util.Asserts;

import java.time.LocalDate;

import static spring.turbo.webmvc.RemoteAddressUtils.getIpAddress;

/**
 * @author 应卓
 *
 * @since 1.2.3
 */
public final class AuthenticationDetailsImpl implements AuthenticationDetails {

    private final LocalDate authenticatedTime;

    private final String path;

    @Nullable
    private final Token authenticatedToken;

    @Nullable
    private final String clientIp;

    public AuthenticationDetailsImpl(HttpServletRequest request, @Nullable Token token) {
        Asserts.notNull(request);
        Asserts.notNull(token);
        this.authenticatedTime = LocalDate.now();
        this.authenticatedToken = token;
        this.path = request.getRequestURI();
        this.clientIp = getIpAddress(request); // TODO: 没有考虑到 WebFlux
    }

    @Override
    public LocalDate getAuthenticatedTime() {
        return this.authenticatedTime;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Nullable
    @Override
    public Token getAuthenticatedToken() {
        return this.authenticatedToken;
    }

    @Nullable
    @Override
    public String getClientId() {
        return this.clientIp;
    }

}
