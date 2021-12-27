/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.support;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import spring.turbo.util.Asserts;

import java.time.Duration;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class RedisCaptchaDao implements CaptchaDao {

    private final StringRedisTemplate redisTemplate;

    public RedisCaptchaDao(@NonNull StringRedisTemplate template) {
        Asserts.notNull(template);
        this.redisTemplate = template;
    }

    @Override
    public void save(String accessKey, String captchaWord, Duration ttl) {
        if (ttl == null) {
            redisTemplate.opsForValue().set(accessKey, captchaWord);
        } else {
            redisTemplate.opsForValue().set(accessKey, captchaWord, ttl);
        }
    }

    @Override
    public Optional<String> find(String access) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(access));
    }

    @Override
    public void delete(String accessKey) {
        redisTemplate.delete(accessKey);
    }

}
