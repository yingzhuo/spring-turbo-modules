package spring.turbo.module.dataaccessing.datasource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态可切换数据源
 *
 * @author 应卓
 * @see DataSource
 * @see DataSourceSwitch
 * @see DynamicDataSourceRemote
 * @see ThreadLocal
 * @see #builder()
 * @since 1.1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean {

    private DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
    }

    /**
     * 新建创建器
     *
     * @return 创建器实例
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceRemote.getKey();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 创建器
     */
    public static final class Builder {

        private final Map<Object, Object> targetDataSources = new HashMap<>();

        @Nullable
        private DataSource defaultTargetDataSource;

        /**
         * 私有构造方法
         */
        private Builder() {
        }

        public Builder defaultTargetDataSource(DataSource defaultTargetDataSource) {
            this.defaultTargetDataSource = defaultTargetDataSource;
            return this;
        }

        public Builder addTargetDataSources(String name, DataSource targetDataSource) {
            this.targetDataSources.put(name, targetDataSource);
            return this;
        }

        public DynamicDataSource build() {
            return new DynamicDataSource(defaultTargetDataSource, targetDataSources);
        }
    }

}
