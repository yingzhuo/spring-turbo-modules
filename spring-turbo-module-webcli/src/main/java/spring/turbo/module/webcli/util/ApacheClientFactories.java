package spring.turbo.module.webcli.util;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import spring.turbo.util.Asserts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author 应卓
 *
 * @see org.springframework.web.client.RestClient
 * @see org.springframework.web.client.RestTemplate
 *
 * @since 3.3.1
 */
public final class ApacheClientFactories {

    /**
     * 私有构造方法
     */
    private ApacheClientFactories() {
        super();
    }

    public static ClientHttpRequestFactory createClientHttpRequestFactory(Resource certificate, String password) {
        Asserts.notNull(certificate, "certificate is null");

        try {
            var keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(certificate.getInputStream(), password.toCharArray());

            SSLContext sslContext = SSLContextBuilder.create().loadKeyMaterial(keyStore, password.toCharArray())
                    .loadTrustMaterial((X509Certificate[] certificateChain, String authType) -> true).build();

            Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register(URIScheme.HTTPS.getId(),
                            new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                    .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory()).build();

            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                    .setConnectionManagerShared(true).build();

            return new HttpComponentsClientHttpRequestFactory(httpClient);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException
                | UnrecoverableKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static ClientHttpRequestFactory createDefaultClientHttpRequestFactory() {
        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial((X509Certificate[] certificateChain, String authType) -> true).build();

            Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register(URIScheme.HTTPS.getId(),
                            new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                    .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory()).build();

            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                    .setConnectionManagerShared(true).build();

            return new HttpComponentsClientHttpRequestFactory(httpClient);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
