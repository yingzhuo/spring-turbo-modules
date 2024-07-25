package spring.turbo.module.webcli.cli;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ReactorNettyClientRequestFactory;
import org.springframework.lang.Nullable;
import reactor.netty.http.client.HttpClient;
import spring.turbo.module.webcli.x509.TrustAllX509TrustManager;
import spring.turbo.util.crypto.keystore.KeyStoreFormat;

import javax.net.ssl.KeyManagerFactory;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Optional;

/**
 * {@link ClientHttpRequestFactory} Netty 版本实现 <br>
 * <em>注意: 使用本类产生的 ClientHttpRequestFactory 默认使用 {@link spring.turbo.module.webcli.x509.TrustAllX509TrustManager}。自担风险</em>
 *
 * @author 应卓
 * @since 3.3.0
 */
public class NettyClientRequestFactoryBean implements FactoryBean<ClientHttpRequestFactory>, InitializingBean {

    private @Nullable Resource clientSideCertificate;
    private @Nullable KeyStoreFormat clientSideCertificateFormat = KeyStoreFormat.PKCS12;
    private @Nullable String clientSideCertificatePassword;
    private @Nullable Duration connectTimeout = null;
    private @Nullable Duration exchangeTimeout = null;
    private @Nullable Duration readTimeout = null;
    private ReactorNettyClientRequestFactory factory = null;

    /**
     * 默认构造方法
     */
    public NettyClientRequestFactoryBean() {
    }

    public NettyClientRequestFactoryBean(
            @Nullable Resource clientSideCertificate,
            @Nullable KeyStoreFormat clientSideCertificateFormat,
            @Nullable String clientSideCertificatePassword) {

        this(
                clientSideCertificate,
                clientSideCertificateFormat,
                clientSideCertificatePassword,
                null,
                null,
                null
        );
    }

    public NettyClientRequestFactoryBean(
            @Nullable Resource clientSideCertificate,
            @Nullable KeyStoreFormat clientSideCertificateFormat,
            @Nullable String clientSideCertificatePassword,
            @Nullable Duration connectTimeout,
            @Nullable Duration exchangeTimeout,
            @Nullable Duration readTimeout) {


        this.clientSideCertificate = clientSideCertificate;
        this.clientSideCertificateFormat = clientSideCertificateFormat;
        this.clientSideCertificatePassword = clientSideCertificatePassword;
        this.connectTimeout = connectTimeout;
        this.exchangeTimeout = exchangeTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientHttpRequestFactory getObject() {
        return this.factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return ClientHttpRequestFactory.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {

        var httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(createSSLContext(
                                clientSideCertificate, clientSideCertificateFormat, clientSideCertificatePassword)
                        )
                );

        this.factory = new ReactorNettyClientRequestFactory(httpClient);
        Optional.ofNullable(connectTimeout).ifPresent(timeout -> this.factory.setConnectTimeout(timeout));
        Optional.ofNullable(readTimeout).ifPresent(timeout -> this.factory.setReadTimeout(timeout));
        Optional.ofNullable(exchangeTimeout).ifPresent(timeout -> this.factory.setExchangeTimeout(timeout));
    }

    private SslContext createSSLContext(
            @Nullable Resource clientSideCertificate,
            @Nullable KeyStoreFormat clientSideCertificateFormat,
            @Nullable String clientSideCertificatePassword
    ) {
        try {
            var builder = SslContextBuilder.forClient()
                    .trustManager(TrustAllX509TrustManager.getInstance());

            if (clientSideCertificate != null && clientSideCertificatePassword != null) {
                var ksf = Optional.ofNullable(clientSideCertificateFormat)
                        .map(KeyStoreFormat::getValue)
                        .orElseGet(KeyStore::getDefaultType);

                var keyStore = KeyStore.getInstance(ksf);
                keyStore.load(clientSideCertificate.getInputStream(), clientSideCertificatePassword.toCharArray());

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, clientSideCertificatePassword.toCharArray());
                builder.keyManager(keyManagerFactory);
            }

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setConnectTimeout(@Nullable Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setExchangeTimeout(@Nullable Duration exchangeTimeout) {
        this.exchangeTimeout = exchangeTimeout;
    }

    public void setReadTimeout(@Nullable Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setClientSideCertificate(@Nullable Resource clientSideCertificate) {
        this.clientSideCertificate = clientSideCertificate;
    }

    public void setClientSideCertificateFormat(@Nullable KeyStoreFormat clientSideCertificateFormat) {
        this.clientSideCertificateFormat = clientSideCertificateFormat;
    }

    public void setClientSideCertificatePassword(@Nullable String clientSideCertificatePassword) {
        this.clientSideCertificatePassword = clientSideCertificatePassword;
    }

}
