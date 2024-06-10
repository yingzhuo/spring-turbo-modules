/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.cli;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
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
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    private @Nullable Duration connectionConnectTimeout;
    private @Nullable Duration connectionSocketTimeout;
    private @Nullable Duration requestSocketTimeout;

    private ClientHttpRequestFactory factory;

    @Override
    public ClientHttpRequestFactory getObject() {
        return factory;
    }

    @Override
    public Class<?> getObjectType() {
        return ClientHttpRequestFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        var sslContext = createSSLContext(this.clientSideCertificate, this.clientSideCertificatePassword);

        var socketRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register(URIScheme.HTTPS.getId(),
                        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory()).build();

        var httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true);

        if (requestSocketTimeout != null) {
            var requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(requestSocketTimeout.toMillis(), TimeUnit.MILLISECONDS).build();

            httpClientBuilder.setDefaultRequestConfig(requestConfig);
        }

        if (connectionConnectTimeout != null || connectionSocketTimeout != null) {
            final var connConfigBuilder = ConnectionConfig.custom();
            Optional.ofNullable(connectionConnectTimeout).ifPresent(
                    timeout -> connConfigBuilder.setConnectTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS));

            Optional.ofNullable(connectionSocketTimeout).ifPresent(
                    timeout -> connConfigBuilder.setSocketTimeout((int) timeout.toSeconds(), TimeUnit.SECONDS));

            var connectionManager = new BasicHttpClientConnectionManager();
            connectionManager.setConnectionConfig(connConfigBuilder.build());

            httpClientBuilder.setConnectionManager(connectionManager);
        }

        this.factory = new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());
    }

    private SSLContext createSSLContext(@Nullable Resource certificate, @Nullable String password)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException,
            CertificateException, IOException {
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

    public void setConnectionConnectTimeout(@Nullable Duration connectionConnectTimeout) {
        this.connectionConnectTimeout = connectionConnectTimeout;
    }

    public void setConnectionSocketTimeout(@Nullable Duration connectionSocketTimeout) {
        this.connectionSocketTimeout = connectionSocketTimeout;
    }

    public void setRequestSocketTimeout(@Nullable Duration requestSocketTimeout) {
        this.requestSocketTimeout = requestSocketTimeout;
    }

}
