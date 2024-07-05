package spring.turbo.module.dataaccessing.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.dataaccessing.datasource.DataSourceSwitchingAdvice;
import spring.turbo.module.dataaccessing.datasource.DynamicDataSource;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnBean(DynamicDataSource.class)
@ConditionalOnMissingBean(DataSourceSwitchingAdvice.class)
public class DataSourceSwitchingAdviceAutoConfiguration {

    @Bean
    public DataSourceSwitchingAdvice dataSourceSwitchingAspect() {
        return new DataSourceSwitchingAdvice();
    }

}
