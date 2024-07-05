package spring.turbo.module.misc.captcha.support;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.time.Duration;

/**
 * {@link CaptchaDao}的Redis相关实现
 *
 * @author 应卓
 * @since 1.0.1
 */
public class RedisCaptchaDao implements CaptchaDao {

    private final StringRedisTemplate redisTemplate;

    public RedisCaptchaDao(StringRedisTemplate template) {
        Asserts.notNull(template);
        this.redisTemplate = template;
    }

    @Override
    public void save(String accessKey, String captchaWord, @Nullable Duration ttl) {
        Asserts.hasText(accessKey);
        Asserts.hasText(captchaWord);

        if (ttl == null) {
            redisTemplate.opsForValue().set(accessKey, captchaWord);
        } else {
            redisTemplate.opsForValue().set(accessKey, captchaWord, ttl);
        }
    }

    @Override
    public String find(String accessKey) {
        Asserts.hasText(accessKey);
        return redisTemplate.opsForValue().get(accessKey);
    }

    @Override
    public void delete(String accessKey) {
        Asserts.hasText(accessKey);
        redisTemplate.delete(accessKey);
    }

}
