package spring.turbo.module.jwt.autoconfiguration;

import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.jwt.DelegatingTokenService;
import spring.turbo.module.jwt.JsonWebTokenService;
import spring.turbo.module.jwt.factory.delegate.HutoolJsonWebTokenFactory;
import spring.turbo.module.jwt.validator.delegate.HutoolJsonWebTokenValidator;

/**
 * @author 应卓
 * @since 3.1.1
 */
@AutoConfiguration
@ConditionalOnBean(JWTSigner.class)
@ConditionalOnClass(JWTSigner.class)
public class HutoolAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JsonWebTokenService hutoolJsonWebTokenService(JWTSigner signer) {
        return new DelegatingTokenService(
                HutoolJsonWebTokenFactory.of(signer),
                HutoolJsonWebTokenValidator.of(signer)
        );
    }

}
