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
import spring.turbo.util.crypto.MD5Crypt;

/**
 * @author 应卓
 * @since 1.0.13
 */
public class HtpasswdEncoderPassword implements PasswordEncoder {

    private static final String PREFIX = "$apr1$";

    public static void main(String[] args) {
        PasswordEncoder encoder = new HtpasswdEncoderPassword();
        System.out.println(
                encoder.matches("yingzhuo", "$apr1$KBLvXBdP$wMqLNz5nuVMx1EejmigU.0")
        );
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Crypt.apr1Crypt(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return doMatches(rawPassword, encodedPassword);
        } catch (Throwable e) {
            return false;
        }
    }

    private boolean doMatches(CharSequence rawPassword, String encodedPassword) {
        String pwd = encodedPassword;

        if (!encodedPassword.startsWith(PREFIX)) {
            return false;
        }

        pwd = pwd.substring(PREFIX.length());

        final String[] parts = pwd.split("\\$");

        if (parts.length != 2) {
            return false;
        }

        final String salt = parts[0];
        return encodedPassword.equals(MD5Crypt.apr1Crypt(rawPassword.toString(), salt));
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
