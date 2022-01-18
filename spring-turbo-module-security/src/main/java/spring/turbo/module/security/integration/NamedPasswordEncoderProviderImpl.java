/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.integration;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import spring.turbo.module.security.encoder.Base64PasswordEncoder;
import spring.turbo.module.security.encoder.NamedPasswordEncoder;
import spring.turbo.module.security.encoder.NamedPasswordEncoderProvider;
import spring.turbo.util.ListFactories;

import java.util.Collection;

/**
 * @author 应卓
 * @see spring.turbo.module.security.encoder.PasswordEncoderFactories
 * @since 1.0.1
 */
@SuppressWarnings("deprecation")
public class NamedPasswordEncoderProviderImpl implements NamedPasswordEncoderProvider {

    @Override
    public Collection<NamedPasswordEncoder> getPasswordEncoders() {
        return ListFactories.newUnmodifiableList(
                NamedPasswordEncoder.of("bcrypt", new BCryptPasswordEncoder()),
                NamedPasswordEncoder.of("ldap", new LdapShaPasswordEncoder()),
                NamedPasswordEncoder.of("MD4", new Md4PasswordEncoder()),
                NamedPasswordEncoder.of("MD5", new MessageDigestPasswordEncoder("MD5")),
                NamedPasswordEncoder.of("SHA-1", new MessageDigestPasswordEncoder("SHA-1")),
                NamedPasswordEncoder.of("SHA-256", new MessageDigestPasswordEncoder("SHA-256")),
                NamedPasswordEncoder.of("pbkdf2", new Pbkdf2PasswordEncoder()),
                NamedPasswordEncoder.of("scrypt", new SCryptPasswordEncoder()),
                NamedPasswordEncoder.of("argon2", new Argon2PasswordEncoder()),
                NamedPasswordEncoder.of("base64", Base64PasswordEncoder.getInstance()),
                NamedPasswordEncoder.of("noop", NoOpPasswordEncoder.getInstance())
        );
    }

}
