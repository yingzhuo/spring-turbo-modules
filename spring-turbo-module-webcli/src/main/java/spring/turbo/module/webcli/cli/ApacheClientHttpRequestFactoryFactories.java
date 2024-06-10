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
import org.springframework.http.client.ClientHttpRequestFactory;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Objects;

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

    public static ClientHttpRequestFactory createDefault() {
        return create(null, null, null, null, null);
    }

    public static ClientHttpRequestFactory create(@Nullable Resource clientSideCertificate,
            @Nullable String clientSideCertificatePassword) {
        return create(clientSideCertificate, clientSideCertificatePassword, null, null, null);
    }

    public static ClientHttpRequestFactory create(@Nullable Duration connectionConnectTimeout,
            @Nullable Duration connectionSocketTimeout, @Nullable Duration requestSocketTimeout) {
        return create(null, null, connectionConnectTimeout, connectionSocketTimeout, requestSocketTimeout);
    }

    public static ClientHttpRequestFactory create(@Nullable Resource clientSideCertificate,
            @Nullable String clientSideCertificatePassword, @Nullable Duration connectionConnectTimeout,
            @Nullable Duration connectionSocketTimeout, @Nullable Duration requestSocketTimeout) {
        try {
            var factoryBean = new ApacheClientHttpRequestFactoryBean();
            factoryBean.setClientSideCertificate(clientSideCertificate);
            factoryBean.setClientSideCertificatePassword(clientSideCertificatePassword);
            factoryBean.setConnectionConnectTimeout(connectionConnectTimeout);
            factoryBean.setConnectionSocketTimeout(connectionSocketTimeout);
            factoryBean.setRequestSocketTimeout(requestSocketTimeout);
            factoryBean.afterPropertiesSet();
            return Objects.requireNonNull(factoryBean.getObject());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
