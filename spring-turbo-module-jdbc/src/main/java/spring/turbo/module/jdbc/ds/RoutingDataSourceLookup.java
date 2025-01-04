package spring.turbo.module.jdbc.ds;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 路由数据源查找策略
 *
 * @author 应卓
 * @since 3.4.1
 */
public final class RoutingDataSourceLookup {

    private static final ThreadLocal<String> DS_NAME_HOLDER = ThreadLocal.withInitial(() -> null);

    /**
     * 私有构造方法
     */
    private RoutingDataSourceLookup() {
        super();
    }

    public static void set(String dataSourceName) {
        Assert.hasText(dataSourceName, "dataSourceName is required");
        DS_NAME_HOLDER.set(dataSourceName);
    }

    @Nullable
    public static String get() {
        return DS_NAME_HOLDER.get();
    }

    public static void remove() {
        DS_NAME_HOLDER.remove();
    }

}
