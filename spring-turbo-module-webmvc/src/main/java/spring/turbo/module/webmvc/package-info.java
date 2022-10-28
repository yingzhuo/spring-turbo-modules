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
package spring.turbo.module.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.lang.Nullable;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import spring.turbo.module.webmvc.rest.JsonEncoder;
import spring.turbo.module.webmvc.rest.JsonEncodingResponseAdvice;

import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class SpringBootAutoConfiguration implements WebMvcConfigurer {

    @Autowired(required = false)
    public void config(@Nullable BeanNameViewResolver resolver) {
        if (resolver != null) {
            resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RemoteAddressHandlerMethodArgumentResolver());
    }

    @Bean
    @ConditionalOnBean(JsonEncoder.class)
    JsonEncodingResponseAdvice jsonEncodingResponseAdvice(JsonEncoder encoder, ObjectMapper objectMapper) {
        return new JsonEncodingResponseAdvice(encoder, objectMapper);
    }

}
