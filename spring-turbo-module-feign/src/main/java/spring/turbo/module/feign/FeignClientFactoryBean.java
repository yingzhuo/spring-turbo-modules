/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import feign.Request;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.OrderComparator;
import org.springframework.core.env.Environment;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.module.feign.annotation.*;
import spring.turbo.util.Asserts;
import spring.turbo.util.InstanceCache;

import java.util.LinkedList;
import java.util.List;

import static feign.Feign.Builder;

/**
 * @author 应卓
 * @since 1.0.0
 */
class FeignClientFactoryBean implements SmartFactoryBean, InitializingBean, ApplicationContextAware, EnvironmentAware {

    private Class<?> clientType; // setter
    private String url; // setter
    private Environment environment; // 回调
    private InstanceCache instanceCache; // 回调

    // 本类必须有public默认构造
    public FeignClientFactoryBean() {
        super();
    }

    @Override
    public Object getObject() {
        Builder builder = new Builder();
        decoded404(builder, clientType);
        logging(builder, clientType);
        encoderAndDecoder(builder, clientType);
        errorDecoder(builder, clientType);
        queryMapEncoder(builder, clientType);
        client(builder, clientType);
        contract(builder, clientType);
        capabilities(builder, clientType);
        options(builder, clientType);
        doNotCloseAfterDecode(builder, clientType);
        requestInterceptors(builder, clientType);
        customizer(builder, clientType);
        return builder.target(clientType, url);
    }

    protected void decoded404(final Builder builder, final Class<?> clientType) {
        if (AnnotationUtils.findAnnotation(clientType, Decoded404.class) != null) {
            builder.decode404();
        }
    }

    protected void logging(final Builder builder, final Class<?> clientType) {
        Logging annotation = AnnotationUtils.findAnnotation(clientType, Logging.class);
        if (annotation != null) {
            builder.logger(instanceCache.findOrCreate(annotation.type()));
            builder.logLevel(annotation.level());
        }
    }

    protected void encoderAndDecoder(final Builder builder, final Class<?> clientType) {
        EncoderAndDecoder annotation = AnnotationUtils.findAnnotation(clientType, EncoderAndDecoder.class);
        if (annotation != null) {
            Encoder encoder = instanceCache.findOrCreate(annotation.encoderType());
            Decoder decoder = instanceCache.findOrCreate(annotation.decoderType());
            builder.encoder(encoder);
            builder.decoder(decoder);
        }
    }

    protected void errorDecoder(final Builder builder, final Class<?> clientType) {
        ErrorDecoder annotation = AnnotationUtils.findAnnotation(clientType, ErrorDecoder.class);
        if (annotation != null) {
            builder.errorDecoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    protected void client(final Builder builder, final Class<?> clientType) {
        Client annotation = AnnotationUtils.findAnnotation(clientType, Client.class);
        if (annotation != null) {
            builder.client(instanceCache.findOrCreate(annotation.type()));
        }
    }

    protected void queryMapEncoder(final Builder builder, final Class<?> clientType) {
        QueryMapEncoder annotation = AnnotationUtils.findAnnotation(clientType, QueryMapEncoder.class);
        if (annotation != null) {
            builder.queryMapEncoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    protected void contract(final Builder builder, final Class<?> clientType) {
        Contract annotation = AnnotationUtils.findAnnotation(clientType, Contract.class);
        if (annotation != null) {
            builder.contract(instanceCache.findOrCreate(annotation.type()));
        }
    }

    protected void capabilities(final Builder builder, final Class<?> clientType) {
        Capabilities annotation = AnnotationUtils.findAnnotation(clientType, Capabilities.class);
        if (annotation != null) {
            for (Class<? extends feign.Capability> type : annotation.types()) {
                if (type != null) {
                    builder.addCapability(instanceCache.findOrCreate(type));
                }
            }
        }
    }

    protected void options(final Builder builder, final Class<?> clientType) {
        Options annotation = AnnotationUtils.findAnnotation(clientType, Options.class);
        if (annotation != null) {
            Request.Options ops = new Request.Options(
                    annotation.connectTimeout(),
                    annotation.connectTimeoutUnit(),
                    annotation.readTimeout(),
                    annotation.readTimeoutUnit(),
                    annotation.followRedirects()
            );
            builder.options(ops);
        }
    }

    protected void doNotCloseAfterDecode(final Builder builder, final Class<?> clientType) {
        DoNotCloseAfterDecode annotation = AnnotationUtils.findAnnotation(clientType, DoNotCloseAfterDecode.class);
        if (annotation != null) {
            builder.doNotCloseAfterDecode();
        }
    }

    protected void requestInterceptors(final Builder builder, final Class<?> clientType) {
        RequestInterceptors annotation = AnnotationUtils.findAnnotation(clientType, RequestInterceptors.class);
        if (annotation != null) {
            List<RequestInterceptor> interceptors = new LinkedList<>();
            for (Class<? extends feign.RequestInterceptor> type : annotation.types()) {
                if (type != null) {
                    interceptors.add(instanceCache.findOrCreate(type));
                }
            }
            OrderComparator.sort(interceptors);
            builder.requestInterceptors(interceptors);
        }
    }

    protected void customizer(final Builder builder, final Class<?> clientType) {
        Customizer annotation = AnnotationUtils.findAnnotation(clientType, Customizer.class);
        if (annotation != null && annotation.value() != null) {
            BuilderCustomizer bean = instanceCache.findOrCreate(annotation.value());
            bean.customize(builder);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return clientType;
    }

    @Override
    public boolean isEagerInit() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.notNull(clientType);
        Asserts.notNull(environment);
        Asserts.notNull(url);
        this.url = environment.resolvePlaceholders(url);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.instanceCache = InstanceCache.newInstance(applicationContext);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Class<?> getClientType() {
        return clientType;
    }

    public void setClientType(Class<?> clientType) {
        this.clientType = clientType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
