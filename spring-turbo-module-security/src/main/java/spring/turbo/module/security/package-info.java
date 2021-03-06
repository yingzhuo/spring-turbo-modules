/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
@NonNullApi
@NonNullFields
package spring.turbo.module.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.turbo.core.SpringContext;
import spring.turbo.module.security.event.MaliciousRequestFailureEvent;
import spring.turbo.module.security.exception.MaliciousRequestException;
import spring.turbo.webmvc.AbstractServletFilter;
import spring.turbo.webmvc.SkippableFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringSecurity???DSL ?????????????????????Filter
 *
 * @author ??????
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

            if (filter instanceof SkippableFilter) {
                ((AbstractServletFilter) filter).addSkipPredicates(configuration.skipPredicates());
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
 * @author ??????
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class SpringBootAutoConfiguration implements WebMvcConfigurer {

    /**
     * ?????????HttpFirewall
     *
     * @return HttpFireWall??????
     * @see HttpFirewall
     */
    @Bean
    @ConditionalOnMissingBean
    HttpFirewall httpFirewall() {
        final StrictHttpFirewall bean = new StrictHttpFirewall();
        bean.setAllowSemicolon(true);
        return bean;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param authenticationEventPublisher ???????????????????????????
     * @since 1.0.4
     */
    @Autowired(required = false)
    void configAuthenticationEventPublisher(AuthenticationEventPublisher authenticationEventPublisher) {
        if (authenticationEventPublisher instanceof DefaultAuthenticationEventPublisher) {
            final DefaultAuthenticationEventPublisher publisher = (DefaultAuthenticationEventPublisher) authenticationEventPublisher;

            final Map<Class<? extends AuthenticationException>, Class<? extends AbstractAuthenticationFailureEvent>> mappings =
                    new HashMap<>();
            mappings.put(MaliciousRequestException.class, MaliciousRequestFailureEvent.class);
            publisher.setAdditionalExceptionMappings(mappings);
        }
    }

}
