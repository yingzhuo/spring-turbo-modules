package spring.turbo.module.misc.captcha.support;

import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * @author 应卓
 * @since 1.0.1
 */
public interface CaptchaDao {

    public default void save(String accessKey, String captchaWord) {
        save(accessKey, captchaWord, null);
    }

    public void save(String accessKey, String captchaWord, @Nullable Duration ttl);

    @Nullable
    public String find(String accessKey);

    public void delete(String accessKey);

}
