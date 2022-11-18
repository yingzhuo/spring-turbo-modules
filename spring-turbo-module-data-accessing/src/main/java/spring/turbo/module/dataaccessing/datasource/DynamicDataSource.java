/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dataaccessing.datasource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

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

    public static class Builder implements spring.turbo.bean.Builder<DynamicDataSource> {

        private final Map<Object, Object> targetDataSources = new HashMap<>();

        @Nullable
        private DataSource defaultTargetDataSource;

        /**
         * 私有构造方法
         */
        private Builder() {
            super();
        }

        public Builder defaultTargetDataSource(DataSource defaultTargetDataSource) {
            Asserts.notNull(defaultTargetDataSource);
            this.defaultTargetDataSource = defaultTargetDataSource;
            return this;
        }

        public Builder addTargetDataSources(String name, DataSource targetDataSource) {
            Asserts.hasText(name);
            Asserts.notNull(targetDataSource);
            this.targetDataSources.put(name, targetDataSource);
            return this;
        }

        @Override
        public DynamicDataSource build() {
            Asserts.notNull(defaultTargetDataSource);
            Asserts.notEmpty(targetDataSources);
            return new DynamicDataSource(defaultTargetDataSource, targetDataSources);
        }
    }

}
