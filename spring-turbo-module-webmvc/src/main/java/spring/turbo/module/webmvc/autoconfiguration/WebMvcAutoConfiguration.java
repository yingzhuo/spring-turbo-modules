/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.autoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import spring.turbo.module.webmvc.databinding.DataBinderInitializingAdvice;
import spring.turbo.module.webmvc.support.argument.RemoteAddressHandlerMethodArgumentResolver;
import spring.turbo.module.webmvc.support.argument.SharedObjectHandlerMethodArgumentResolver;

import java.util.List;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Autowired(required = false)
    public void config(@Nullable BeanNameViewResolver resolver) {
        if (resolver != null) {
            resolver.setOrder(HIGHEST_PRECEDENCE);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RemoteAddressHandlerMethodArgumentResolver());
        resolvers.add(new SharedObjectHandlerMethodArgumentResolver());
    }

    /**
     * @since 2024-06-30
     */
    @Bean
    public DataBinderInitializingAdvice dataBinderInitializingAdvice() {
        return new DataBinderInitializingAdvice();
    }

}
