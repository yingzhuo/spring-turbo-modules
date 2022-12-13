/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.core.SpringUtils;
import spring.turbo.util.Asserts;

/**
 * {@link PasswordEncoder} 相关工具
 *
 * @author 应卓
 * @since 2.0.1
 */
public final class PasswordEncoderUtils {

    /**
     * 私有构造方法
     */
    private PasswordEncoderUtils() {
        super();
    }

    public String encode(CharSequence rawPwd) {
        Asserts.notNull(rawPwd);
        return SpringUtils.getRequiredBean(PasswordEncoder.class).encode(rawPwd);
    }

    public boolean matches(CharSequence rawPwd, String encodedPassword) {
        Asserts.notNull(rawPwd);
        Asserts.notNull(encodedPassword);
        return SpringUtils.getRequiredBean(PasswordEncoder.class).matches(rawPwd, encodedPassword);
    }

}
