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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import spring.turbo.core.SpringContext;

import javax.servlet.Filter;
import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
@AutoConfigureAfter(SpringBootAutoConfiguration.class)
class HttpSecurityDSL extends AbstractHttpConfigurer<HttpSecurityDSL, HttpSecurity> {

    @SuppressWarnings({"rawtypes", "unchecked"})
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
                case AT:
                    http.addFilterAt(filter, position);
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
        StrictHttpFirewall bean = new StrictHttpFirewall();
        bean.setAllowSemicolon(true);
        return bean;
    }
}
