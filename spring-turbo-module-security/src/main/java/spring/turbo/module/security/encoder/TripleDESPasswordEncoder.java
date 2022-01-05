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
import spring.turbo.util.crypto.TripleDES;

/**
 * 3DES - PasswordEncoder
 *
 * @author 应卓
 * @see PasswordEncoder
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @since 1.0.0
 */
public class TripleDESPasswordEncoder implements PasswordEncoder {

    private final TripleDES des;

    public TripleDESPasswordEncoder(TripleDES des) {
        Asserts.notNull(des);
        this.des = des;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return des.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return des.decrypt(encodedPassword).contentEquals(rawPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
