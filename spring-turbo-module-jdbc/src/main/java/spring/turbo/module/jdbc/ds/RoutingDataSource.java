package spring.turbo.module.jdbc.ds;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由数据源
 *
 * @author 应卓
 * @see AbstractRoutingDataSource
 * @see RoutingDataSourceLookup
 * @since 3.4.1
 */
public class RoutingDataSource extends AbstractRoutingDataSource implements DataSource, InitializingBean {

    public RoutingDataSource(DataSource defaultDataSource, Map<String, DataSource> targetDataSources) {
        Assert.notNull(defaultDataSource, "defaultDataSource is required");
        Assert.notEmpty(targetDataSources, "targetDataSources is null or empty");

        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.setDataSourceLookup(new MapDataSourceLookup());
    }

    @Override
    @Nullable
    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceLookup.get();
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

}
