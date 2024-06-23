/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.autoconfiguration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import spring.turbo.module.security.FilterConfiguration;

/**
 * SpringSecurity DSL
 *
 * @author 应卓
 * @since 1.3.0
 */
@SuppressWarnings({"unchecked"})
public class SpringSecurityAutoConfigurationDSL
        extends AbstractHttpConfigurer<SpringSecurityAutoConfigurationDSL, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        var ctx = http.getSharedObject(ApplicationContext.class);

        // 核心配置 (多个)
        var configurations = ctx.getBeansOfType(FilterConfiguration.class).values();

        for (var configuration : configurations) {

            // 如果没有启用则跳过
            var env = ctx.getEnvironment();
            var args = ctx.getBean(ApplicationArguments.class);
            if (!configuration.isEnabled(env, args)) {
                continue;
            }

            // 获取过滤器实例
            var filter = configuration.create();
            if (filter == null) {
                continue;
            }

            // 尝试初始化
            if (filter instanceof InitializingBean initializingBean) {
                initializingBean.afterPropertiesSet();
            }

            var position = configuration.positionInChain();
            var beforeOrAfter = configuration.position();

            switch (beforeOrAfter) {
                case BEFORE -> http.addFilterBefore(filter, position);
                case AFTER -> http.addFilterAfter(filter, position);
                case REPLACE -> http.addFilterAt(filter, position);
                default -> throw new AssertionError(); // 不可能运行到此处
            }
        }
    }

}
