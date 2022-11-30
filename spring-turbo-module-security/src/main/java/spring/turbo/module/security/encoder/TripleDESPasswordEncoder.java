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
import spring.turbo.util.crypto.TripleDES;

/**
 * @author 应卓
 * @since 2.0.1
 */
public final class TripleDESPasswordEncoder implements PasswordEncoder {

    private final TripleDES _3des;

    public TripleDESPasswordEncoder(String password, String salt) {
        this._3des = TripleDES.builder()
                .passwordAndSalt(password, salt)
                .build();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return this._3des.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this._3des.decrypt(encodedPassword).equals(rawPassword.toString());
    }

}
