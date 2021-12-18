/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.module.feign.annotation.Decoded404;
import spring.turbo.module.feign.annotation.Slf4j;
import spring.turbo.util.Asserts;

import java.util.Optional;

import static feign.Feign.Builder;

/**
 * @author 应卓
 * @since 1.0.0
 */
class FeignClientFactoryBean implements SmartFactoryBean, InitializingBean, EnvironmentAware {

    private Class<?> clientType; // setter
    private String url; // setter
    private Environment environment; // 回调

    // 本类必须有public默认构造
    public FeignClientFactoryBean() {
        super();
    }

    @Override
    public Object getObject() {
        Builder builder = new Builder();
        decoded404(builder, clientType);
        slf4j(builder, clientType);
        return builder.target(clientType, url);
    }

    private void decoded404(Builder builder, Class<?> clientType) {
        if (AnnotationUtils.findAnnotation(clientType, Decoded404.class) != null) {
            builder.decode404();
        }
    }

    private void slf4j(Builder builder, Class<?> clientType) {
        Optional.ofNullable(AnnotationUtils.findAnnotation(clientType, Slf4j.class))
                .ifPresent(a -> {
                    builder.logger(new Slf4jLogger());
                    builder.logLevel(a.level());
                });
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
