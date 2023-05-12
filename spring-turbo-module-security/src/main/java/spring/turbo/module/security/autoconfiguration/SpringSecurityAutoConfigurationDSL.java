/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.autoconfiguration;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import spring.turbo.core.SpringContext;
import spring.turbo.module.security.FilterConfiguration;

import java.util.List;

/**
 * SpringSecurity DSL
 *
 * @author 应卓
 *
 * @since 1.3.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SpringSecurityAutoConfigurationDSL
        extends AbstractHttpConfigurer<SpringSecurityAutoConfigurationDSL, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final SpringContext ctx = this.getSpringContext(http);

        final List<FilterConfiguration> configurations = ctx.getBeanList(FilterConfiguration.class);

        for (final FilterConfiguration configuration : configurations) {

            // 如果没有启用则跳过
            if (!configuration.isEnabled()) {
                continue;
            }

            // 获取过滤器实例
            final Filter filter = configuration.create();
            if (filter == null) {
                continue;
            }

            // 尝试初始化
            if (filter instanceof InitializingBean initializingBeanFilter) {
                initializingBeanFilter.afterPropertiesSet();
            }

            final var position = configuration.positionInChain();
            final var beforeOrAfter = configuration.position();

            switch (beforeOrAfter) {
            case BEFORE -> http.addFilterBefore(filter, position);
            case AFTER -> http.addFilterAfter(filter, position);
            case REPLACE -> http.addFilterAt(filter, position);
            default -> throw new AssertionError(); // 不可能运行到此处
            }
        }
    }

    private SpringContext getSpringContext(HttpSecurity http) {
        return SpringContext.of(http.getSharedObject(ApplicationContext.class));
    }

}
