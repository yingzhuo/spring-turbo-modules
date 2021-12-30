/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.event;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import spring.turbo.module.security.filter.TokenAuthenticationFilter;

/**
 * 签名被篡改或签名算法不匹配时，此事件将被{@link TokenAuthenticationFilter}传播
 *
 * @author 应卓
 * @see TokenAuthenticationFilter
 * @see spring.turbo.module.security.exception.InvalidSignatureException
 * @since 1.0.4
 */
public class InvalidSignatureFailureEvent extends AbstractAuthenticationFailureEvent {

    public InvalidSignatureFailureEvent(Authentication authentication, AuthenticationException exception) {
        super(authentication, exception);
    }

}
