/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.autoconfiguration;

import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.jwt.DelegatingTokenService;
import spring.turbo.module.jwt.JsonWebTokenService;
import spring.turbo.module.jwt.factory.JsonWebTokenFactory;
import spring.turbo.module.jwt.factory.delegate.HutoolJsonWebTokenFactory;
import spring.turbo.module.jwt.validator.JsonWebTokenValidator;
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
    public JsonWebTokenFactory hutoolJsonWebTokenFactory(JWTSigner signer) {
        return HutoolJsonWebTokenFactory.of(signer);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonWebTokenValidator hutoolJsonWebTokenValidator(JWTSigner signer) {
        return HutoolJsonWebTokenValidator.of(signer);
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonWebTokenService hutoolJsonWebTokenService(JsonWebTokenFactory factory, JsonWebTokenValidator validator) {
        return new DelegatingTokenService(factory, validator);
    }

}
