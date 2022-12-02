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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.queryselector.property.SelectorSetProperties;
import spring.turbo.module.queryselector.sql.WhereClauseBuilder;
import spring.turbo.module.queryselector.sql.WhereClauseBuilderImpl;

/**
 * @author 应卓
 * @since 2.0.1
 */
@AutoConfiguration
@ConditionalOnClass(name = "freemarker.template.Template")
@ConditionalOnMissingBean(WhereClauseBuilder.class)
@ConditionalOnProperty(prefix = "springturbo.selector-set-formatter.sql", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SelectorSetProperties.class)
public class WhereClauseBuilderAutoConfiguration {

    @Bean
    public WhereClauseBuilder whereClauseBuilder(SelectorSetProperties properties) {
        return new WhereClauseBuilderImpl(properties.getSql().getItemNameToTableColumnMappings());
    }

}
