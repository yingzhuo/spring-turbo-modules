/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.support;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 应卓
 * @since 1.0.1
 */
class SpringBootAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    AccessKeyGenerator captchaAccessKeyGenerator() {
        return new SimpleAccessKeyGenerator();
    }

}

/**
 * @author 应卓
 * @since 1.0.1
 */
@ConditionalOnBean(type = "org.springframework.data.redis.core.StringRedisTemplate")
class SpringBootAutoConfigurationDependsOnSpringDataRedis {

    @Bean
    @ConditionalOnMissingBean
    CaptchaDao captchaDao(StringRedisTemplate template) {
        return new RedisCaptchaDao(template);
    }

}