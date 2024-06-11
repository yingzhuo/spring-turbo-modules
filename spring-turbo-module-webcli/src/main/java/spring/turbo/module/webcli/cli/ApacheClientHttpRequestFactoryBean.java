/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.cli;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;

/**
 * {@link ClientHttpRequestFactory} 的 <a href="https://hc.apache.org/httpcomponents-client-ga/"></a> 版本的实现
 *
 * @author 应卓
 *
 * @see org.springframework.web.client.RestClient
 * @see org.springframework.web.client.RestTemplate
 *
 * @since 3.3.1
 */
public class ApacheClientHttpRequestFactoryBean implements FactoryBean<ClientHttpRequestFactory>, InitializingBean {

    private @Nullable Resource clientSideCertificate;
    private @Nullable String clientSideCertificatePassword;
    private @Nullable Duration connectTimeout;
    private @Nullable Duration requestTimeout;
    private HttpComponentsClientHttpRequestFactory factory;

    /**
     * 默认构造方法
     */
    public ApacheClientHttpRequestFactoryBean() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientHttpRequestFactory getObject() {
        return factory;
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
    public void afterPropertiesSet() throws Exception {
        var sslContext = createSSLContext(clientSideCertificate, clientSideCertificatePassword);

        var socketRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register(URIScheme.HTTPS.getId(),
                        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory()).build();

        var httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true);

        this.factory = new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
        Optional.ofNullable(this.requestTimeout).ifPresent(d -> factory.setConnectionRequestTimeout(d));
        Optional.ofNullable(this.connectTimeout).ifPresent(d -> factory.setConnectTimeout(d));
    }

    private SSLContext createSSLContext(@Nullable Resource certificate, @Nullable String password) throws Exception {
        KeyStore keyStore = null;

        if (certificate != null && password != null) {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(certificate.getInputStream(), password.toCharArray());
        }

        var contextBuilder = SSLContextBuilder.create()
                .loadTrustMaterial((X509Certificate[] certificateChain, String authType) -> true);
        if (keyStore != null) {
            contextBuilder.loadKeyMaterial(keyStore, password.toCharArray());
        }
        return contextBuilder.build();
    }

    public void setClientSideCertificate(@Nullable Resource clientSideCertificate) {
        this.clientSideCertificate = clientSideCertificate;
    }

    public void setClientSideCertificatePassword(@Nullable String clientSideCertificatePassword) {
        this.clientSideCertificatePassword = clientSideCertificatePassword;
    }

    public void setRequestTimeout(@Nullable Duration requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public void setConnectTimeout(@Nullable Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

}
