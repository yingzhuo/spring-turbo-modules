/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
 *
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
