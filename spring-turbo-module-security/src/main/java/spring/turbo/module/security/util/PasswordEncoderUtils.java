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
    }

    public String encode(CharSequence rawPwd) {
        Asserts.notNull(rawPwd);
        return SpringUtils.getRequiredBean(PasswordEncoder.class).encode(rawPwd);
    }

    public boolean matches(CharSequence rawPwd, String encodedPwd) {
        Asserts.notNull(rawPwd);
        Asserts.notNull(encodedPwd);
        return SpringUtils.getRequiredBean(PasswordEncoder.class).matches(rawPwd, encodedPwd);
    }

}
