/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import spring.turbo.autoconfiguration.properties.SelectorSetProps;
import spring.turbo.module.queryselector.formatter.SelectorFormatter;
import spring.turbo.module.queryselector.formatter.SelectorSetFormatter;

/**
 * @author 应卓
 *
 * @since 2.0.1
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "springturbo.selector-set-formatter", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SelectorSetProps.class)
public class QuerySetCoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SelectorFormatter selectorFormatter(SelectorSetProps properties) {
        final SelectorFormatter bean = new SelectorFormatter();
        bean.setSeparatorInSelector(properties.getSelectorFormatter().getSeparatorInSelector());
        bean.setSeparatorInRange(properties.getSelectorFormatter().getSeparatorInRange());
        bean.setSeparatorInSet(properties.getSelectorFormatter().getSeparatorInSet());
        bean.setDatePattern(properties.getSelectorFormatter().getDatePattern());
        bean.setDatetimePattern(properties.getSelectorFormatter().getDatetimePattern());
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public SelectorSetFormatter selectorSetResolver(SelectorSetProps properties, SelectorFormatter selectorFormatter) {
        final SelectorSetFormatter bean = new SelectorSetFormatter();
        bean.setSeparatorBetweenSelectors(properties.getSeparatorBetweenSelectors());
        bean.setIgnoreErrorIfUnableToParse(properties.isIgnoreErrorIfUnableToParse());
        bean.setIgnoreErrorIfUnableToPrint(properties.isIgnoreErrorIfUnableToPrint());
        bean.setSelectorFormatter(selectorFormatter);
        return bean;
    }

}
