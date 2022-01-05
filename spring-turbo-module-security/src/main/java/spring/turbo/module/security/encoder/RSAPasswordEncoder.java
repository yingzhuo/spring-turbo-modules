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
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.RSA;

import java.util.Objects;

/**
 * RSA - PasswordEncoder
 *
 * @author 应卓
 * @see PasswordEncoder
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @since 1.0.0
 */
public class RSAPasswordEncoder implements PasswordEncoder {

    private final RSA rsa;

    public RSAPasswordEncoder(RSA rsa) {
        Asserts.notNull(rsa);
        this.rsa = rsa;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return rsa.encryptByPrivateKey(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(rawPassword.toString(), rsa.decryptByPublicKey(encodedPassword));
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
