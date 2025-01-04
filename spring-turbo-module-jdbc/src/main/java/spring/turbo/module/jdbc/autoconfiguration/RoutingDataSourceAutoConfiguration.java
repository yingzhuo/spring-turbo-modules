package spring.turbo.module.jdbc.autoconfiguration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import spring.turbo.module.jdbc.ds.DataSourceSwitchAdvice;
import spring.turbo.module.jdbc.ds.RoutingDataSource;
import spring.turbo.module.jdbc.ds.RoutingDataSourceProperties;
import spring.turbo.module.jdbc.util.DataSourceFactories;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author 应卓
 * @since 3.4.1
 */
@EnableConfigurationProperties(RoutingDataSourceProperties.class)
@ConditionalOnProperty(prefix = "springturbo.routing-data-source", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RoutingDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RoutingDataSource.class)
    public DataSource dataSource(RoutingDataSourceProperties props) {
        final var targetDataSources = new HashMap<String, DataSource>();

        props.getHikariDataSources().forEach((dataSourceName, dataSourceConfig) -> {
            var dataSource = (HikariDataSource) DataSourceFactories.createDataSource(dataSourceConfig, HikariDataSource.class);

            dataSource.setPoolName(dataSourceConfig.getPoolName());
            dataSource.setMinimumIdle(dataSourceConfig.getMinimumIdle());
            dataSource.setMaximumPoolSize(dataSourceConfig.getMaximumPoolSize());
            dataSource.setAutoCommit(dataSourceConfig.isAutoCommit());
            dataSource.setIdleTimeout(dataSourceConfig.getIdleTimeout());
            dataSource.setMaxLifetime(dataSourceConfig.getMaxLifetime());
            dataSource.setConnectionTimeout(dataSourceConfig.getConnectionTimeout());
            dataSource.setConnectionTestQuery(dataSourceConfig.getConnectionTestQuery());
            dataSource.setValidationTimeout(dataSourceConfig.getValidationTimeout());
            dataSource.setConnectionInitSql(dataSourceConfig.getConnectionInitSql());
            dataSource.setInitializationFailTimeout(dataSourceConfig.getInitializationFailTimeout());

            targetDataSources.put(dataSourceName, dataSource);
        });

        var defaultDataSource = targetDataSources.get(props.getDefaultDataSourceName());
        Assert.notNull(defaultDataSource, "unable to find defaultDataSource");

        return new RoutingDataSource(defaultDataSource, targetDataSources);
    }

    @Bean
    @ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
    @ConditionalOnMissingBean(DataSourceSwitchAdvice.class)
    public DataSourceSwitchAdvice dataSourceSwitchAdvice() {
        return new DataSourceSwitchAdvice(Ordered.HIGHEST_PRECEDENCE);
    }

}
