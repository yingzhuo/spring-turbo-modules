/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.util.crypto.Base64;

/**
 * 仅仅能用于测试，不可使用在生产环境
 *
 * @author 应卓
 * @since 1.0.2
 */
public final class Base64PasswordEncoder implements PasswordEncoder {

    private static final Base64PasswordEncoder INSTANCE = new Base64PasswordEncoder();

    public static Base64PasswordEncoder getInstance() {
        return INSTANCE;
    }

    private Base64PasswordEncoder() {
        super();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return Base64.toString(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return true;
    }

}
