package spring.turbo.module.jdbc.util;

import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * 数据源创建工具
 *
 * @author 应卓
 * @since 3.4.1
 */
public final class DataSourceFactories {

    /**
     * 私有构造方法
     */
    private DataSourceFactories() {
    }

    public static <T> T createDataSource(JdbcConnectionDetails connectionDetails, Class<? extends DataSource> type) {
        return createDataSource(connectionDetails, type, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createDataSource(JdbcConnectionDetails connectionDetails, Class<? extends DataSource> type, @Nullable ClassLoader classLoader) {
        // @formatter:off
        return (T) DataSourceBuilder.create(Objects.requireNonNullElse(classLoader, ClassUtils.getDefaultClassLoader()))
                .type(type)
                .driverClassName(connectionDetails.getDriverClassName())
                .url(connectionDetails.getJdbcUrl())
                .username(connectionDetails.getUsername())
                .password(connectionDetails.getPassword())
                .build();
        // @formatter:on
    }

}
