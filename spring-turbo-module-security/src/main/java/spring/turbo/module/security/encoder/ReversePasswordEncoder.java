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

import java.util.Objects;

/**
 * @author 应卓
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @since 1.0.0
 */
public class ReversePasswordEncoder implements PasswordEncoder {

    public static final ReversePasswordEncoder INSTANCE = new ReversePasswordEncoder();

    private ReversePasswordEncoder() {
        super();
    }

    public static ReversePasswordEncoder getInstance() {
        return INSTANCE;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return new StringBuilder(rawPassword.toString())
                .reverse()
                .toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Objects.equals(rawPassword, encodedPassword);
    }

}
