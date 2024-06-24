/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.autoconfiguration;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.jwt.DelegatingTokenService;
import spring.turbo.module.jwt.JsonWebTokenService;
import spring.turbo.module.jwt.factory.JsonWebTokenFactory;
import spring.turbo.module.jwt.factory.delegate.JavaJwtJsonWebTokenFactory;
import spring.turbo.module.jwt.validator.JsonWebTokenValidator;
import spring.turbo.module.jwt.validator.delegate.JavaJwtJsonWebTokenValidator;

/**
 * @author 应卓
 * @since 3.1.1
 */
@AutoConfiguration
@ConditionalOnBean(Algorithm.class)
@ConditionalOnClass(Algorithm.class)
public class JavaJwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JsonWebTokenFactory javaJwtJsonWebTokenFactory(Algorithm algorithm) {
        return JavaJwtJsonWebTokenFactory.of(algorithm);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonWebTokenValidator javaJwtJsonWebTokenValidator(Algorithm algorithm) {
        return JavaJwtJsonWebTokenValidator.of(algorithm);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonWebTokenService javaJwtJsonWebTokenService(JsonWebTokenFactory factory, JsonWebTokenValidator validator) {
        return new DelegatingTokenService(factory, validator);
    }

}
