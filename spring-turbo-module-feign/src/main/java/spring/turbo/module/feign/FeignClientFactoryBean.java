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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.OrderComparator;
import org.springframework.core.env.Environment;
import spring.turbo.bean.AbstractFactoryBean;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.module.feign.annotation.*;
import spring.turbo.module.feign.retryer.RetryerImpl;
import spring.turbo.util.Asserts;
import spring.turbo.util.DurationParseUtils;
import spring.turbo.util.InstanceCache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static feign.Feign.Builder;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
class FeignClientFactoryBean extends AbstractFactoryBean
        implements InitializingBean, ApplicationContextAware, EnvironmentAware {

    private String url; // setter
    private Environment environment; // 回调
    private InstanceCache instanceCache; // 回调

    /**
     * 默认构造方法
     */
    public FeignClientFactoryBean() {
        super();
    }

    @Override
    public Object getObject() {
        Builder builder = new Builder();
        this.decoded404(builder, classDefinitionResolvable.getBeanClass());
        this.logging(builder, classDefinitionResolvable.getBeanClass());
        this.encoderAndDecoder(builder, classDefinitionResolvable.getBeanClass());
        this.errorDecoder(builder, classDefinitionResolvable.getBeanClass());
        this.queryMapEncoder(builder, classDefinitionResolvable.getBeanClass());
        this.client(builder, classDefinitionResolvable.getBeanClass());
        this.contract(builder, classDefinitionResolvable.getBeanClass());
        this.capabilities(builder, classDefinitionResolvable.getBeanClass());
        this.options(builder, classDefinitionResolvable.getBeanClass());
        this.doNotCloseAfterDecode(builder, classDefinitionResolvable.getBeanClass());
        this.requestInterceptors(builder, classDefinitionResolvable.getBeanClass());
        this.retryer(builder, classDefinitionResolvable.getBeanClass());
        this.customizer(builder, classDefinitionResolvable.getBeanClass());
        return builder.target(classDefinitionResolvable.getBeanClass(), url);
    }

    private void decoded404(final Builder builder, final Class<?> clientType) {
        if (AnnotationUtils.findAnnotation(clientType, Decoded404.class) != null) {
            builder.decode404();
        }
    }

    private void logging(final Builder builder, final Class<?> clientType) {
        Logging annotation = AnnotationUtils.findAnnotation(clientType, Logging.class);
        if (annotation != null) {
            builder.logger(instanceCache.findOrCreate(annotation.type()));
            builder.logLevel(annotation.level());
        }
    }

    private void encoderAndDecoder(final Builder builder, final Class<?> clientType) {
        EncoderAndDecoder annotation = AnnotationUtils.findAnnotation(clientType, EncoderAndDecoder.class);
        if (annotation != null) {
            Encoder encoder = instanceCache.findOrCreate(annotation.encoderType());
            Decoder decoder = instanceCache.findOrCreate(annotation.decoderType());
            builder.encoder(encoder);
            builder.decoder(decoder);
        }
    }

    private void errorDecoder(final Builder builder, final Class<?> clientType) {
        ErrorDecoder annotation = AnnotationUtils.findAnnotation(clientType, ErrorDecoder.class);
        if (annotation != null) {
            builder.errorDecoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    private void client(final Builder builder, final Class<?> clientType) {
        Client annotation = AnnotationUtils.findAnnotation(clientType, Client.class);
        if (annotation != null) {
            builder.client(instanceCache.findOrCreate(annotation.type()));
        }
    }

    private void queryMapEncoder(final Builder builder, final Class<?> clientType) {
        QueryMapEncoder annotation = AnnotationUtils.findAnnotation(clientType, QueryMapEncoder.class);
        if (annotation != null) {
            builder.queryMapEncoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    private void contract(final Builder builder, final Class<?> clientType) {
        Contract annotation = AnnotationUtils.findAnnotation(clientType, Contract.class);
        if (annotation != null) {
            builder.contract(instanceCache.findOrCreate(annotation.type()));
        }
    }

    private void capabilities(final Builder builder, final Class<?> clientType) {
        Capabilities annotation = AnnotationUtils.findAnnotation(clientType, Capabilities.class);
        if (annotation != null) {
            for (Class<? extends feign.Capability> type : annotation.types()) {
                if (type != null) {
                    builder.addCapability(instanceCache.findOrCreate(type));
                }
            }
        }
    }

    private void options(final Builder builder, final Class<?> clientType) {
        Options annotation = AnnotationUtils.findAnnotation(clientType, Options.class);
        if (annotation != null) {
            Request.Options ops = new Request.Options(
                    DurationParseUtils.parse(annotation.connectTimeout()).toMillis(),
                    TimeUnit.MILLISECONDS,
                    DurationParseUtils.parse(annotation.readTimeout()).toMillis(),
                    TimeUnit.MILLISECONDS,
                    annotation.followRedirects()
            );
            builder.options(ops);
        }
    }

    private void doNotCloseAfterDecode(final Builder builder, final Class<?> clientType) {
        DoNotCloseAfterDecode annotation = AnnotationUtils.findAnnotation(clientType, DoNotCloseAfterDecode.class);
        if (annotation != null) {
            builder.doNotCloseAfterDecode();
        }
    }

    private void requestInterceptors(final Builder builder, final Class<?> clientType) {
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

    private void retryer(final Builder builder, final Class<?> clientType) {
        Retryer annotation = AnnotationUtils.findAnnotation(clientType, Retryer.class);
        if (annotation != null) {
            builder.retryer(new RetryerImpl(
                    DurationParseUtils.parse(annotation.period()).toMillis(),
                    DurationParseUtils.parse(annotation.maxPeriod()).toMillis(),
                    annotation.maxAttempts()
            ));
        } else {
            builder.retryer(feign.Retryer.NEVER_RETRY);
        }
    }

    private void customizer(final Builder builder, final Class<?> clientType) {
        Customizer annotation = AnnotationUtils.findAnnotation(clientType, Customizer.class);
        if (annotation != null && annotation.value() != null) {
            BuilderCustomizer bean = instanceCache.findOrCreate(annotation.value());
            bean.customize(builder);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.notNull(classDefinitionResolvable);
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

    public void setUrl(String url) {
        this.url = url;
    }

}
