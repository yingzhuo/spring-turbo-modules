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
class FeignClientFactoryBean extends AbstractFactoryBean
        implements InitializingBean, ApplicationContextAware, EnvironmentAware {

    private String url; // setter
    private Environment environment; //  XxxAware 回调
    private InstanceCache instanceCache; //  XxxAware 回调

    /**
     * 默认构造方法
     */
    public FeignClientFactoryBean() {
        super();
    }

    @Override
    public Object getObject() {
        Asserts.notNull(classDef);

        // client 类型 (interface)
        final var clientType = classDef.getBeanClass();

        final var builder = new Builder();
        this.client(builder, clientType);
        this.contract(builder, clientType); // 合约
        this.decoded404(builder, clientType);
        this.logging(builder, clientType);
        this.encoderAndDecoder(builder, clientType);
        this.errorDecoder(builder, clientType);
        this.queryMapEncoder(builder, clientType);
        this.capabilities(builder, clientType);
        this.options(builder, clientType);
        this.doNotCloseAfterDecode(builder, clientType);
        this.requestInterceptors(builder, clientType);
        this.retryer(builder, clientType);
        this.exceptionPropagationPolicy(builder, clientType);
        this.invocationHandlerFactory(builder, clientType);
        this.customizer(builder, clientType);
        return builder.target(clientType, url);
    }

    @SuppressWarnings("deprecation")
    private void decoded404(final Builder builder, final Class<?> clientType) {
        if (AnnotationUtils.findAnnotation(clientType, Decoded404.class) != null) {
            builder.decode404();
        }
    }

    // logging
    private void logging(final Builder builder, final Class<?> clientType) {
        Logging annotation = AnnotationUtils.findAnnotation(clientType, Logging.class);
        if (annotation != null) {
            builder.logger(instanceCache.findOrCreate(annotation.type()));
            builder.logLevel(annotation.level());
        }
    }

    // encoder
    // decoder
    private void encoderAndDecoder(final Builder builder, final Class<?> clientType) {
        EncoderAndDecoder annotation = AnnotationUtils.findAnnotation(clientType, EncoderAndDecoder.class);
        if (annotation != null) {
            Encoder encoder = instanceCache.findOrCreate(annotation.encoderType());
            Decoder decoder = instanceCache.findOrCreate(annotation.decoderType());
            builder.encoder(encoder);
            builder.decoder(decoder);
        }
    }

    // 错误处理
    private void errorDecoder(final Builder builder, final Class<?> clientType) {
        ErrorDecoder annotation = AnnotationUtils.findAnnotation(clientType, ErrorDecoder.class);
        if (annotation != null) {
            builder.errorDecoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 客户端
    private void client(final Builder builder, final Class<?> clientType) {
        Client annotation = AnnotationUtils.findAnnotation(clientType, Client.class);
        if (annotation != null) {
            builder.client(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 指定QueryMapperEncoder
    private void queryMapEncoder(final Builder builder, final Class<?> clientType) {
        QueryMapEncoder annotation = AnnotationUtils.findAnnotation(clientType, QueryMapEncoder.class);
        if (annotation != null) {
            builder.queryMapEncoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 合约
    private void contract(final Builder builder, final Class<?> clientType) {
        Contract annotation = AnnotationUtils.findAnnotation(clientType, Contract.class);
        if (annotation != null) {
            builder.contract(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 监控相关
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

    // 性能选项
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

    // 关闭连接选项
    private void doNotCloseAfterDecode(final Builder builder, final Class<?> clientType) {
        DoNotCloseAfterDecode annotation = AnnotationUtils.findAnnotation(clientType, DoNotCloseAfterDecode.class);
        if (annotation != null) {
            builder.doNotCloseAfterDecode();
        }
    }

    // 拦截器
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

    // 异常传播策略
    private void exceptionPropagationPolicy(final Builder builder, final Class<?> clientType) {
        ExceptionPropagationPolicy annotation = AnnotationUtils.findAnnotation(clientType, ExceptionPropagationPolicy.class);
        if (annotation != null) {
            builder.exceptionPropagationPolicy(annotation.value());
        }
    }

    // 代理相关
    private void invocationHandlerFactory(final Builder builder, final Class<?> clientType) {
        InvocationHandlerFactory annotation = AnnotationUtils.findAnnotation(clientType, InvocationHandlerFactory.class);
        if (annotation != null) {
            builder.invocationHandlerFactory(instanceCache.findOrCreate(annotation.value()));
        }
    }

    // 重试相关
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

    // 自由客制化
    private void customizer(final Builder builder, final Class<?> clientType) {
        Customizer annotation = AnnotationUtils.findAnnotation(clientType, Customizer.class);
        if (annotation != null) {
            BuilderCustomizer bean = instanceCache.findOrCreate(annotation.value());
            bean.customize(builder);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.notNull(classDef);
        Asserts.notNull(environment);
        Asserts.notNull(url);
        this.url = environment.resolvePlaceholders(url);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
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
