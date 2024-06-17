/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import feign.Feign;
import feign.Request;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.core.OrderComparator;
import spring.turbo.module.feign.annotation.*;
import spring.turbo.module.feign.retryer.RetryerImpl;
import spring.turbo.util.DurationParseUtils;
import spring.turbo.util.InstanceCache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static spring.turbo.core.AnnotationFinder.findAnnotation;

/**
 * @author 应卓
 * @since 2.0.9
 */
@SuppressWarnings({"deprecation", "rawtypes"})
final class FeignClientSupplier implements Supplier {

    private final InstanceCache instanceCache;
    private final Class<?> clientType;
    private final String url;

    public FeignClientSupplier(InstanceCache instanceCache, Class<?> clientType, String url) {
        this.instanceCache = instanceCache;
        this.clientType = clientType;
        this.url = url;
    }

    @Override
    public Object get() {
        var builder = new Feign.Builder();
        this.setupClient(builder, clientType);
        this.setupContract(builder, clientType);
        this.setupDecoded404(builder, clientType);
        this.setupLogging(builder, clientType);
        this.setupEncoderAndDecoder(builder, clientType);
        this.setupErrorDecoder(builder, clientType);
        this.setupQueryMapEncoder(builder, clientType);
        this.setupCapabilities(builder, clientType);
        this.setupOptions(builder, clientType);
        this.setupDoNotCloseAfterDecode(builder, clientType);
        this.setupRequestInterceptors(builder, clientType);
        this.setupRetryer(builder, clientType);
        this.setupExceptionPropagationPolicy(builder, clientType);
        this.setupInvocationHandlerFactory(builder, clientType);
        this.setupCustomizer(builder, clientType);
        return builder.target(clientType, url);
    }

    private void setupDecoded404(final Feign.Builder builder, final Class<?> clientType) {
        if (findAnnotation(clientType, Decoded404.class) != null) {
            builder.decode404();
        }
    }

    // logging
    private void setupLogging(final Feign.Builder builder, final Class<?> clientType) {
        Logging annotation = findAnnotation(clientType, Logging.class);
        if (annotation != null) {
            builder.logger(instanceCache.findOrCreate(annotation.type()));
            builder.logLevel(annotation.level());
        }
    }

    // encoder
    // decoder
    private void setupEncoderAndDecoder(final Feign.Builder builder, final Class<?> clientType) {
        EncoderAndDecoder annotation = findAnnotation(clientType, EncoderAndDecoder.class);
        if (annotation != null) {
            Encoder encoder = instanceCache.findOrCreate(annotation.encoderType());
            Decoder decoder = instanceCache.findOrCreate(annotation.decoderType());
            builder.encoder(encoder);
            builder.decoder(decoder);
        }
    }

    // 错误处理
    private void setupErrorDecoder(final Feign.Builder builder, final Class<?> clientType) {
        ErrorDecoder annotation = findAnnotation(clientType, ErrorDecoder.class);
        if (annotation != null) {
            builder.errorDecoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 客户端
    private void setupClient(final Feign.Builder builder, final Class<?> clientType) {
        Client annotation = findAnnotation(clientType, Client.class);
        if (annotation != null) {
            builder.client(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 指定QueryMapperEncoder
    private void setupQueryMapEncoder(final Feign.Builder builder, final Class<?> clientType) {
        QueryMapEncoder annotation = findAnnotation(clientType, QueryMapEncoder.class);
        if (annotation != null) {
            builder.queryMapEncoder(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 合约
    private void setupContract(final Feign.Builder builder, final Class<?> clientType) {
        Contract annotation = findAnnotation(clientType, Contract.class);
        if (annotation != null) {
            builder.contract(instanceCache.findOrCreate(annotation.type()));
        }
    }

    // 监控相关
    private void setupCapabilities(final Feign.Builder builder, final Class<?> clientType) {
        Capabilities annotation = findAnnotation(clientType, Capabilities.class);
        if (annotation != null) {
            for (Class<? extends feign.Capability> type : annotation.types()) {
                if (type != null) {
                    builder.addCapability(instanceCache.findOrCreate(type));
                }
            }
        }
    }

    // 性能选项
    private void setupOptions(final Feign.Builder builder, final Class<?> clientType) {
        Options annotation = findAnnotation(clientType, Options.class);
        if (annotation != null) {
            Request.Options ops = new Request.Options(DurationParseUtils.parse(annotation.connectTimeout()).toMillis(),
                    TimeUnit.MILLISECONDS, DurationParseUtils.parse(annotation.readTimeout()).toMillis(),
                    TimeUnit.MILLISECONDS, annotation.followRedirects());
            builder.options(ops);
        }
    }

    // 关闭连接选项
    private void setupDoNotCloseAfterDecode(final Feign.Builder builder, final Class<?> clientType) {
        DoNotCloseAfterDecode annotation = findAnnotation(clientType, DoNotCloseAfterDecode.class);
        if (annotation != null) {
            builder.doNotCloseAfterDecode();
        }
    }

    // 拦截器
    private void setupRequestInterceptors(final Feign.Builder builder, final Class<?> clientType) {
        RequestInterceptors annotation = findAnnotation(clientType, RequestInterceptors.class);
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
    private void setupExceptionPropagationPolicy(final Feign.Builder builder, final Class<?> clientType) {
        ExceptionPropagationPolicy annotation = findAnnotation(clientType, ExceptionPropagationPolicy.class);
        if (annotation != null) {
            builder.exceptionPropagationPolicy(annotation.value());
        }
    }

    // 代理相关
    private void setupInvocationHandlerFactory(final Feign.Builder builder, final Class<?> clientType) {
        InvocationHandlerFactory annotation = findAnnotation(clientType, InvocationHandlerFactory.class);
        if (annotation != null) {
            builder.invocationHandlerFactory(instanceCache.findOrCreate(annotation.value()));
        }
    }

    // 重试相关
    private void setupRetryer(final Feign.Builder builder, final Class<?> clientType) {
        Retryer annotation = findAnnotation(clientType, Retryer.class);
        if (annotation != null) {
            builder.retryer(new RetryerImpl(DurationParseUtils.parse(annotation.period()).toMillis(),
                    DurationParseUtils.parse(annotation.maxPeriod()).toMillis(), annotation.maxAttempts()));
        } else {
            builder.retryer(feign.Retryer.NEVER_RETRY);
        }
    }

    // 自由客制化
    private void setupCustomizer(final Feign.Builder builder, final Class<?> clientType) {
        Customizer annotation = findAnnotation(clientType, Customizer.class);
        if (annotation != null) {
            BuilderCustomizer bean = instanceCache.findOrCreate(annotation.value());
            bean.customize(builder);
        }
    }

}
