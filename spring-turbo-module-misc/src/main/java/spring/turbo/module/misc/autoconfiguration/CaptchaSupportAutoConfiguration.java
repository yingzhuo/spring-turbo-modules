package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.misc.captcha.support.AccessKeyGenerator;
import spring.turbo.module.misc.captcha.support.SimpleAccessKeyGenerator;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(AccessKeyGenerator.class)
public class CaptchaSupportAutoConfiguration {

    @Bean
    public AccessKeyGenerator captchaAccessKeyGenerator() {
        return new SimpleAccessKeyGenerator();
    }

}
