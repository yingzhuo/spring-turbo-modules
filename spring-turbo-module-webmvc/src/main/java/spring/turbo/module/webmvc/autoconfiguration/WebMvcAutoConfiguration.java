package spring.turbo.module.webmvc.autoconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import spring.turbo.module.webmvc.databinding.DataBinderInitializingAdvice;
import spring.turbo.module.webmvc.support.argument.RemoteAddressHandlerMethodArgumentResolver;

import java.util.List;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@EnableConfigurationProperties(SpringBootWebMvcProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Autowired(required = false)
    public void configBeanNameViewResolver(@Nullable BeanNameViewResolver resolver) {
        if (resolver != null) {
            resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RemoteAddressHandlerMethodArgumentResolver());

        // 对ThreadLocal使用不当有可能产生资源泄露问题
        // 从3.4.0开始，不再自动注册此组件
        // resolvers.add(new SharedObjectHandlerMethodArgumentResolver());
    }

    /**
     * @since 2024-06-30
     */
    @Bean
    @ConditionalOnProperty(prefix = "springturbo.webmvc", name = "data-binder-initializing-advice", havingValue = "true", matchIfMissing = true)
    public DataBinderInitializingAdvice dataBinderInitializingAdvice() {
        return new DataBinderInitializingAdvice();
    }

}
