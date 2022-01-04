/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.AES;

import java.util.Objects;

/**
 * @author 应卓
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @since 1.0.0
 */
public class AESPasswordEncoder implements PasswordEncoder {

    private final AES aes;

    public AESPasswordEncoder(@NonNull AES aes) {
        Asserts.notNull(aes);
        this.aes = aes;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return aes.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(rawPassword.toString(), aes.decrypt(encodedPassword));
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
