/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datasource;

import org.springframework.lang.Nullable;

/**
 * 全局数据源切换遥控器
 *
 * @author 应卓
 * @since 1.1.0
 */
public class DynamicDataSourceRemote {

    private static final ThreadLocal<String> HOLDER = new InheritableThreadLocal();

    /**
     * 私有构造方法
     */
    private DynamicDataSourceRemote() {
        super();
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
