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
import org.springframework.http.client.ReactorNettyClientRequestFactory;
import spring.turbo.util.CastUtils;
import spring.turbo.util.crypto.KeyStoreFormat;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * {@link ReactorNettyClientRequestFactory} 生成工具
 *
 * @author 应卓
 * @since 3.3.0
 */
public final class NettyClientRequestFactoryFactories {

    /**
     * 私有构造方法
     */
    private NettyClientRequestFactoryFactories() {
        super();
    }

    /**
     * 创建 {@link ReactorNettyClientRequestFactory} 对象
     *
     * @return {@link ReactorNettyClientRequestFactory} 对象
     */
    public static ReactorNettyClientRequestFactory create() {
        return create(null, null, null, null, null, null);
    }

    /**
     * 创建 {@link ReactorNettyClientRequestFactory} 对象
     *
     * @param clientSideCertificate         SSL客户端证书
     * @param clientSideCertificateFormat   SSL客户端证书类型
     * @param clientSideCertificatePassword SSL客户端证书密码
     * @return {@link ReactorNettyClientRequestFactory} 对象
     */
    public static ReactorNettyClientRequestFactory create(
            @Nullable Resource clientSideCertificate,
            @Nullable KeyStoreFormat clientSideCertificateFormat,
            @Nullable String clientSideCertificatePassword) {

        return create(
                clientSideCertificate,
                clientSideCertificateFormat,
                clientSideCertificatePassword,
                null,
                null,
                null
        );
    }

    /**
     * 创建 {@link ReactorNettyClientRequestFactory} 对象
     *
     * @param connectTimeout  连接超时时间
     * @param exchangeTimeout 请求超时时间
     * @param readTimout      读取应答超时时间
     * @return {@link ReactorNettyClientRequestFactory} 对象
     */
    public static ReactorNettyClientRequestFactory create(
            @Nullable Duration connectTimeout,
            @Nullable Duration exchangeTimeout,
            @Nullable Duration readTimout
    ) {
        return create(null, null, null,
                connectTimeout, exchangeTimeout, readTimout
        );
    }

    /**
     * 创建 {@link ReactorNettyClientRequestFactory} 对象
     *
     * @param clientSideCertificate         SSL客户端证书
     * @param clientSideCertificateFormat   SSL客户端证书类型
     * @param clientSideCertificatePassword SSL客户端证书密码
     * @param connectTimeout                连接超时时间
     * @param exchangeTimeout               请求超时时间
     * @param readTimout                    读取应答超时时间
     * @return {@link ReactorNettyClientRequestFactory} 对象
     */
    public static ReactorNettyClientRequestFactory create(
            @Nullable Resource clientSideCertificate,
            @Nullable KeyStoreFormat clientSideCertificateFormat,
            @Nullable String clientSideCertificatePassword,
            @Nullable Duration connectTimeout,
            @Nullable Duration exchangeTimeout,
            @Nullable Duration readTimout
    ) {
        try {
            var factoryBean = new NettyClientRequestFactoryBean();
            factoryBean.setClientSideCertificate(clientSideCertificate);
            factoryBean.setClientSideCertificateFormat(clientSideCertificateFormat);
            factoryBean.setClientSideCertificatePassword(clientSideCertificatePassword);
            factoryBean.setConnectTimeout(connectTimeout);
            factoryBean.setExchangeTimeout(exchangeTimeout);
            factoryBean.setReadTimeout(readTimout);
            factoryBean.afterPropertiesSet();
            var beanObject = factoryBean.getObject();
            if (beanObject == null) {
                throw new IllegalArgumentException("Cannot create ReactorNettyClientRequestFactory instance");
            }
            return CastUtils.castNonNull(beanObject);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Throwable e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
