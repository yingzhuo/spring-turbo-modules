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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import spring.turbo.module.queryselector.property.QuerySelectorProperties;
import spring.turbo.module.queryselector.resolver.SelectorSetResolver;
import spring.turbo.module.queryselector.resolver.SelectorSetResolverImpl;
import spring.turbo.module.queryselector.resolver.StringToSelectorSetConverter;

/**
 * @author 应卓
 * @since 1.1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(QuerySelectorProperties.class)
@ConditionalOnProperty(prefix = "springturbo.queryselector", name = "enabled", havingValue = "true", matchIfMissing = true)
class SpringBootAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    SelectorSetResolver selectorSetResolver(final QuerySelectorProperties properties) {
        final SelectorSetResolverImpl bean = new SelectorSetResolverImpl();
        bean.setSeparatorBetweenSelectors(properties.getSeparatorBetweenSelectors());
        bean.setSeparatorInSelector(properties.getSeparatorInSelector());
        bean.setSeparatorInRange(properties.getSeparatorInRange());
        bean.setSeparatorInSet(properties.getSeparatorInSet());
        bean.setDatePattern(properties.getDatePattern());
        bean.setDatetimePattern(properties.getDatetimePattern());
        bean.setSkipErrorIfUnableToResolve(properties.isSkipErrorIfUnableToResolve());
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    StringToSelectorSetConverter stringToSelectorSetConverter(SelectorSetResolver resolver) {
        return new StringToSelectorSetConverter(resolver);
    }

}
