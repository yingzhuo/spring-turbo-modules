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
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import spring.turbo.util.ServiceLoaderUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public final class PasswordEncoderFactories {

    private PasswordEncoderFactories() {
        super();
    }

    @NonNull
    public static PasswordEncoder createDelegatingPasswordEncoder() {
        return createDelegatingPasswordEncoder("bcrypt");
    }

    @NonNull
    public static PasswordEncoder createDelegatingPasswordEncoder(@NonNull String encodingId) {
        final Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("ldap", new LdapShaPasswordEncoder());
        encoders.put("MD4", new Md4PasswordEncoder());
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        // service load 在这里起作用
        encoders.putAll(loadFromModules());

        final DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(encodingId, encoders);
        encoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return encoder;
    }

    // since 1.0.1
    private static Map<String, PasswordEncoder> loadFromModules() {
        final Map<String, PasswordEncoder> map = new HashMap<>();
        final List<NamedPasswordEncoderProvider> providers = ServiceLoaderUtils.loadQuietly(NamedPasswordEncoderProvider.class);

        for (NamedPasswordEncoderProvider provider : providers) {
            Collection<NamedPasswordEncoder> encoders = provider.getPasswordEncoders();
            if (encoders != null) {
                for (NamedPasswordEncoder encoder : encoders) {
                    if (encoder != null) {
                        map.put(encoder.getName(), encoder);
                    }
                }
            }
        }
        return map;
    }

}
