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
 * {@link CaptchaDao}的Redis相关实现
 *
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
    public void save(@NonNull String accessKey, @NonNull String captchaWord, Duration ttl) {
        Asserts.hasText(accessKey);
        Asserts.hasText(captchaWord);

        if (ttl == null) {
            redisTemplate.opsForValue().set(accessKey, captchaWord);
        } else {
            redisTemplate.opsForValue().set(accessKey, captchaWord, ttl);
        }
    }

    @Override
    public Optional<String> find(@NonNull String accessKey) {
        Asserts.hasText(accessKey);

        return Optional.ofNullable(redisTemplate.opsForValue().get(accessKey));
    }

    @Override
    public void delete(@NonNull String accessKey) {
        Asserts.hasText(accessKey);
        redisTemplate.delete(accessKey);
    }

}
