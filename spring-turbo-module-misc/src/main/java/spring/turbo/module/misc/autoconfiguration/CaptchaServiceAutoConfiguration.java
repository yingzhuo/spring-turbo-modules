package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.misc.captcha.CaptchaService;
import spring.turbo.module.misc.captcha.google.GoogleCaptchaService;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(CaptchaService.class)
public class CaptchaServiceAutoConfiguration {

    @Bean
    public CaptchaService captchaService() {
        return new GoogleCaptchaService();
    }

}
