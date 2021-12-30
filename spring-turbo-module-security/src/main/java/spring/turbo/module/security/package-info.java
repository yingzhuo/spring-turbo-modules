/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import spring.turbo.core.SpringContext;
import spring.turbo.module.security.event.InvalidSignatureFailureEvent;
import spring.turbo.module.security.exception.InvalidSignatureException;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@AutoConfigureAfter(SpringBootAutoConfiguration.class)
class HttpSecurityDSL extends AbstractHttpConfigurer<HttpSecurityDSL, HttpSecurity> {

    public void configure(HttpSecurity http) {
        final SpringContext ctx = this.getSpringContext(http);

        final List<FilterConfiguration> configurations = ctx.getBeanList(FilterConfiguration.class);

        for (FilterConfiguration configuration : configurations) {
            final Filter filter = configuration.create();
            if (filter == null) {
                continue;
            }

            if (filter instanceof InitializingBean) {
                try {
                    ((InitializingBean) filter).afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }

            final FilterConfiguration.Position beforeOrAfter = configuration.position();
            final Class<Filter> position = configuration.positionInChain();

            switch (beforeOrAfter) {
                case BEFORE:
                    http.addFilterBefore(filter, position);
                    break;
                case AFTER:
                    http.addFilterAfter(filter, position);
                    break;
                case AT:
                    http.addFilterAt(filter, position);
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }

    private SpringContext getSpringContext(HttpSecurity http) {
        return SpringContext.of(http.getSharedObject(ApplicationContext.class));
    }

}

/**
 * @author 应卓
 * @since 1.0.0
 */
class SpringBootAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    HttpFirewall httpFirewall() {
        final StrictHttpFirewall bean = new StrictHttpFirewall();
        bean.setAllowSemicolon(true);
        return bean;
    }

    // since 1.0.4
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ApplicationEventPublisher.class)
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publisher) {
        final Map<Class<? extends AuthenticationException>, Class<? extends AbstractAuthenticationFailureEvent>> mappings =
                new HashMap<>();
        mappings.put(InvalidSignatureException.class, InvalidSignatureFailureEvent.class);

        final DefaultAuthenticationEventPublisher bean = new DefaultAuthenticationEventPublisher(publisher);
        bean.setAdditionalExceptionMappings(mappings);
        return bean;
    }

}
