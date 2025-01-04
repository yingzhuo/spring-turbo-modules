package spring.turbo.module.jdbc.ds;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 应卓
 * @since 3.4.1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "springturbo.routing-data-source")
public class RoutingDataSourceProperties implements Serializable {

    private boolean enabled = true;
    private String defaultDataSourceName;
    private Map<String, HikariProperties> hikariDataSources;

}
