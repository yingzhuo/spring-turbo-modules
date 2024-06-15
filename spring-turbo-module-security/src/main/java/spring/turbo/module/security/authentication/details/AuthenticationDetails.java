/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication.details;

import org.springframework.lang.Nullable;
import spring.turbo.module.security.token.Token;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author 应卓
 *
 * @see spring.turbo.module.security.authentication.RequestDetailsProvider
 * @see org.springframework.security.authentication.AbstractAuthenticationToken#setDetails(Object)
 *
 * @since 1.2.3
 */
public sealed interface AuthenticationDetails extends Serializable permits AuthenticationDetailsImpl {

    /**
     * 认证时间
     *
     * @return 认证时间
     */
    public LocalDate getAuthenticatedTime();

    /**
     * 请求的path
     *
     * @return path
     */
    public String getPath();

    /**
     * 认证时的令牌对象
     *
     * @return 令牌对象或 {@code null}
     */
    @Nullable
    public Token getAuthenticatedToken();

    /**
     * 远程IP地址
     *
     * @return 远程IP地址或 {@code null}
     */
    @Nullable
    public String getClientId();

}
