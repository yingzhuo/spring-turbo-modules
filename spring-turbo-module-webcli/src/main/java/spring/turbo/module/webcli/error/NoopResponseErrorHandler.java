package spring.turbo.module.webcli.error;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * @author 应卓
 * @see #getInstance()
 * @see org.springframework.web.client.NoOpResponseErrorHandler
 * @see org.springframework.web.client.DefaultResponseErrorHandler
 * @since 3.3.1
 */
public final class NoopResponseErrorHandler implements ResponseErrorHandler {

    /**
     * 私有构造方法
     */
    private NoopResponseErrorHandler() {
    }

    /**
     * 获取 {@link NoopResponseErrorHandler} 实例
     *
     * @return {@link NoopResponseErrorHandler} 实例
     */
    public static NoopResponseErrorHandler getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) {
        // no operation
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    private static class SyncAvoid {
        private static final NoopResponseErrorHandler INSTANCE = new NoopResponseErrorHandler();
    }

}
