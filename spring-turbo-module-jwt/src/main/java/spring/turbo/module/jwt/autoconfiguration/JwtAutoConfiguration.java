package spring.turbo.module.jwt.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.jwt.JwtService;
import spring.turbo.module.jwt.JwtServiceImpl;
import spring.turbo.module.jwt.alg.JwtSigner;

/**
 * @author 应卓
 * @since 3.3.2
 */
@AutoConfiguration
@ConditionalOnBean(JwtSigner.class)
@ConditionalOnMissingBean(JwtService.class)
public class JwtAutoConfiguration {

    @Bean
    public JwtService jsonWebTokenService(JwtSigner signer) {
        return new JwtServiceImpl(signer);
    }

}
