/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.support;

import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * @author 应卓
 *
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
