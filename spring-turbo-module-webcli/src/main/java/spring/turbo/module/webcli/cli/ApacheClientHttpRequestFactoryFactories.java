/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.cli;

import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author 应卓
 *
 * @since 3.3.1
 */
public final class ApacheClientHttpRequestFactoryFactories {

    /**
     * 私有构造方法
     */
    private ApacheClientHttpRequestFactoryFactories() {
        super();
    }

    /**
     * 创建 {@link HttpComponentsClientHttpRequestFactory} 默认对象
     *
     * @return {@link HttpComponentsClientHttpRequestFactory} 默认对象
     */
    public static HttpComponentsClientHttpRequestFactory create() {
        return create(null, null, null, null);
    }

    /**
     * 创建 {@link HttpComponentsClientHttpRequestFactory} 对象
     *
     * @param connectTimeout
     *            连接超时时间
     * @param requestTimeout
     *            请求超时时间
     *
     * @return {@link HttpComponentsClientHttpRequestFactory} 对象
     */
    public static HttpComponentsClientHttpRequestFactory create(@Nullable Duration connectTimeout,
            @Nullable Duration requestTimeout) {
        return create(null, null, connectTimeout, requestTimeout);
    }

    /**
     * 创建 {@link HttpComponentsClientHttpRequestFactory} 对象
     *
     * @param clientSideCertificate
     *            SSL客户端证书 (PKCS12格式)
     * @param clientSideCertificatePassword
     *            SSL客户端证书密码
     *
     * @return {@link HttpComponentsClientHttpRequestFactory} 对象
     */
    public static HttpComponentsClientHttpRequestFactory create(@Nullable Resource clientSideCertificate,
            @Nullable String clientSideCertificatePassword) {
        return create(clientSideCertificate, clientSideCertificatePassword, null, null);
    }

    /**
     * 创建 {@link HttpComponentsClientHttpRequestFactory} 对象
     *
     * @param clientSideCertificate
     *            SSL客户端证书 (PKCS12格式)
     * @param clientSideCertificatePassword
     *            SSL客户端证书密码
     * @param connectTimeout
     *            连接超时时间
     * @param requestTimeout
     *            请求超时时间
     *
     * @return {@link HttpComponentsClientHttpRequestFactory} 对象
     */
    public static HttpComponentsClientHttpRequestFactory create(@Nullable Resource clientSideCertificate,
            @Nullable String clientSideCertificatePassword, @Nullable Duration connectTimeout,
            @Nullable Duration requestTimeout) {
        try {
            var factoryBean = new ApacheClientHttpRequestFactoryBean();
            factoryBean.setClientSideCertificate(clientSideCertificate);
            factoryBean.setClientSideCertificatePassword(clientSideCertificatePassword);
            factoryBean.setConnectTimeout(connectTimeout);
            factoryBean.setRequestTimeout(requestTimeout);
            factoryBean.afterPropertiesSet();
            var beanObject = (HttpComponentsClientHttpRequestFactory) factoryBean.getObject();
            if (beanObject == null) {
                throw new AssertionError();
            }
            return beanObject;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
