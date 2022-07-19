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
package spring.turbo.module.queryselector.sql;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import spring.turbo.bean.condition.ConditionalOnModule;
import spring.turbo.integration.Modules;
import spring.turbo.module.queryselector.resolver.SelectorSetResolver;
import spring.turbo.module.queryselector.sql.property.ItemNameToTableColumnMap;

/**
 * @author 应卓
 * @since 1.1.2
 */
@AutoConfiguration
@EnableConfigurationProperties(ItemNameToTableColumnMap.class)
@ConditionalOnModule(Modules.SPRING_TURBO_QUERYSELECTOR)
@ConditionalOnBean(SelectorSetResolver.class)
class SpringBootAutoConfiguration {

    @Bean
    public WhereClauseBuilder whereClauseBuilder(ItemNameToTableColumnMap itemNameToTableColumnMap) {
        return new WhereClauseBuilderImpl(itemNameToTableColumnMap);
    }

}
