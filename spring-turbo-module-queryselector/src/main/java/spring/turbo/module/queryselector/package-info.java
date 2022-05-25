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
package spring.turbo.module.queryselector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.jackson.SelectorSetMixin;
import spring.turbo.module.queryselector.resolver.SelectorSetResolver;
import spring.turbo.module.queryselector.resolver.SelectorSetResolverImpl;
import spring.turbo.module.queryselector.resolver.StringToSelectorSetConverter;

/**
 * @author 应卓
 * @since 1.1.0
 */
class SpringBootAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    SelectorSetResolver selectorSetResolver() {
        return new SelectorSetResolverImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    StringToSelectorSetConverter stringToSelectorSetConverter(SelectorSetResolver resolver) {
        return new StringToSelectorSetConverter(resolver);
    }

}

/**
 * @author 应卓
 * @since 1.1.0
 */
@ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
class SpringBootAutoConfigurationJackson {

    @Autowired(required = false)
    void configObjectMapper(@Nullable ObjectMapper om) {
        if (om != null) {
            om.addMixIn(SelectorSet.class, SelectorSetMixin.class);
        }
    }

}
