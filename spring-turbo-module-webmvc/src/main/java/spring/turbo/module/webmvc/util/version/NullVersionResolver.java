package spring.turbo.module.webmvc.util.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 2.0.9
 */
public final class NullVersionResolver implements VersionResolver {

    /**
     * 私有构造方法
     */
    private NullVersionResolver() {
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

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    private static class SyncAvoid {
        private static final NullVersionResolver INSTANCE = new NullVersionResolver();
    }

}
