/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import spring.turbo.lang.Singleton;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 2.0.9
 */
@Singleton
public final class NullVersionResolver implements VersionResolver {

    /**
     * 私有构造方法
     */
    private NullVersionResolver() {
        super();
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static NullVersionResolver getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        return null;
    }

    // 延迟加载
    private static class SyncAvoid {
        private static final NullVersionResolver INSTANCE = new NullVersionResolver();
    }

}
