package spring.turbo.module.security.token;

import org.springframework.lang.NonNull;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

/**
 * 一直返回empty-optional的令牌解析器实现
 *
 * @author 应卓
 * @see #getInstance()
 * @since 1.0.0
 */
public final class NullTokenResolver implements TokenResolver {

    /**
     * 私有构造方法
     */
    private NullTokenResolver() {
    }

    /**
     * 获取本类单例
     *
     * @return 单例实例
     */
    public static NullTokenResolver getInstance() {
        return SyncAvoid.INSTANCE;
    }

    /**
     * 解析令牌
     *
     * @param request HTTP请求
     * @return empty-optional
     */
    @NonNull
    @Override
    public Optional<Token> resolve(WebRequest request) {
        return Optional.empty();
    }

    /**
     * 排序参数
     *
     * @return 排序值 (最低)
     */
    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    private static class SyncAvoid {
        private static final NullTokenResolver INSTANCE = new NullTokenResolver();
    }

}
