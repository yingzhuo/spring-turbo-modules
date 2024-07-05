package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import spring.turbo.module.misc.captcha.support.CaptchaDao;
import spring.turbo.module.misc.captcha.support.RedisCaptchaDao;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(CaptchaDao.class)
@ConditionalOnBean(type = "org.springframework.data.redis.core.StringRedisTemplate")
public class CaptchaSupportRedisAutoConfiguration {

    @Bean
    public CaptchaDao captchaDao(StringRedisTemplate template) {
        return new RedisCaptchaDao(template);
    }

}
