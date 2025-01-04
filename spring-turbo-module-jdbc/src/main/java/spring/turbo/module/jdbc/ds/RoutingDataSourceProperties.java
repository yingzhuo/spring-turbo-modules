package spring.turbo.module.jdbc.ds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 应卓
 * @since 3.4.1
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "springturbo.routing-data-source")
public class RoutingDataSourceProperties implements Serializable, InitializingBean {

    private boolean enabled = true;
    private String defaultDataSourceName;
    private Map<String, HikariProperties> hikariDataSources;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(defaultDataSourceName, "defaultDataSourceName is required");
        Assert.notEmpty(hikariDataSources, "hikariDataSources is empty");
    }

}
