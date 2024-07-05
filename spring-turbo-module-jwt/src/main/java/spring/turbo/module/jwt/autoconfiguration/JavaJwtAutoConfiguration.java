package spring.turbo.module.jwt.autoconfiguration;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.jwt.DelegatingTokenService;
import spring.turbo.module.jwt.JsonWebTokenService;
import spring.turbo.module.jwt.factory.delegate.JavaJwtJsonWebTokenFactory;
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
    public JsonWebTokenService javaJwtJsonWebTokenService(Algorithm algorithm) {
        return new DelegatingTokenService(
                JavaJwtJsonWebTokenFactory.of(algorithm),
                JavaJwtJsonWebTokenValidator.of(algorithm)
        );
    }

}
