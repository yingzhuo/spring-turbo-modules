package spring.turbo.module.webcli.x509;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * {@link X509TrustManager} 实现，这种实现不会检查任何证书信息。
 * <em>注意: </em> 使用本类不安全
 *
 * @author 应卓
 * @see #getInstance()
 * @since 3.3.0
 */
public final class TrustAllX509TrustManager implements X509TrustManager {

    /**
     * 私有构造方法
     */
    private TrustAllX509TrustManager() {
    }

    /**
     * 获取单例实例
     *
     * @return 实例
     */
    public static TrustAllX509TrustManager getInstance() {
        return SyncAvoid.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        // noop
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        // noop
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 延迟加载
     */
    private static final class SyncAvoid {
        private static final TrustAllX509TrustManager INSTANCE = new TrustAllX509TrustManager();
    }

}
