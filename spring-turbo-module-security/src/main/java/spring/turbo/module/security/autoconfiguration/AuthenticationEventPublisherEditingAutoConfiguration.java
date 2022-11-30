/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.autoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.AuthenticationException;
import spring.turbo.module.security.event.MaliciousRequestFailureEvent;
import spring.turbo.module.security.exception.MaliciousRequestException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(AuthenticationEventPublisher.class)
public class AuthenticationEventPublisherEditingAutoConfiguration {

    @Autowired(required = false)
    public void configAuthenticationEventPublisher(AuthenticationEventPublisher authenticationEventPublisher) {
        if (authenticationEventPublisher instanceof DefaultAuthenticationEventPublisher publisher) {
            final Map<Class<? extends AuthenticationException>, Class<? extends AbstractAuthenticationFailureEvent>> mappings =
                    new HashMap<>();
            mappings.put(MaliciousRequestException.class, MaliciousRequestFailureEvent.class);
            publisher.setAdditionalExceptionMappings(mappings);
        }
    }

}
