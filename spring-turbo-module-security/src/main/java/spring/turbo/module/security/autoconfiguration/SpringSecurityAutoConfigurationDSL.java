/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.autoconfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import spring.turbo.core.SpringContext;
import spring.turbo.module.security.FilterConfiguration;

import javax.servlet.Filter;
import java.util.List;

/**
 * SpringSecurity DSL
 *
 * @author 应卓
 * @since 1.3.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringSecurityAutoConfigurationDSL extends AbstractHttpConfigurer<SpringSecurityAutoConfigurationDSL, HttpSecurity> {

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityAutoConfigurationDSL.class);

    @Override
    public void configure(HttpSecurity http) {
        final SpringContext ctx = this.getSpringContext(http);

        final List<FilterConfiguration> configurations = ctx.getBeanList(FilterConfiguration.class);

        for (FilterConfiguration configuration : configurations) {

            if (!configuration.isEnabled()) {
                continue;
            }

            final Filter filter = configuration.create();
            if (filter == null) {
                continue;
            }

            if (filter instanceof InitializingBean) {
                try {
                    ((InitializingBean) filter).afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
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
                case REPLACE:
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
