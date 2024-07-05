package spring.turbo.module.dataaccessing.datasource;

import org.springframework.lang.Nullable;

/**
 * 全局数据源切换遥控器
 *
 * @author 应卓
 * @since 1.1.0
 */
public class DynamicDataSourceRemote {

    private static final ThreadLocal<String> HOLDER = new InheritableThreadLocal<>();

    /**
     * 私有构造方法
     */
    private DynamicDataSourceRemote() {
    }

    @Nullable
    public static String getKey() {
        return HOLDER.get();
    }

    public static void setKey(@Nullable String key) {
        if (key != null) {
            HOLDER.set(key);
        }
    }

    public static void clear() {
        HOLDER.remove();
    }

}
