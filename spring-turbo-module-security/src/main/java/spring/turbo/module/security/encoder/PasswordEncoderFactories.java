/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import spring.turbo.util.Asserts;
import spring.turbo.util.InstanceUtils;
import spring.turbo.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author 应卓
 * @see EncodingIds
 * @since 1.0.0
 */
public final class PasswordEncoderFactories {

    /**
     * 私有构造方法
     */
    private PasswordEncoderFactories() {
        super();
    }

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder() {
        return createDelegatingPasswordEncoder(EncodingIds.bcrypt, EncodingIds.noop);
    }

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(String encodingId) {
        return createDelegatingPasswordEncoder(encodingId, EncodingIds.noop);
    }

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(String encodingId, @Nullable String defaultPasswordEncoderForMatches) {
        Asserts.hasText(encodingId);
        final Map<String, PasswordEncoder> encoders = getEncoders();
        final DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(encodingId, encoders);

        if (StringUtils.isNotBlank(defaultPasswordEncoderForMatches)) {
            encoder.setDefaultPasswordEncoderForMatches(encoders.get(defaultPasswordEncoderForMatches));
        }
        return encoder;
    }

    @SuppressWarnings("deprecation")
    private static Map<String, PasswordEncoder> getEncoders() {
        final Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(EncodingIds.bcrypt, new BCryptPasswordEncoder()); // default
        encoders.put(EncodingIds.ldap, new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put(EncodingIds.MD4, new org.springframework.security.crypto.password.Md4PasswordEncoder());
        encoders.put(EncodingIds.MD5, new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
        encoders.put(EncodingIds.noop, org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put(EncodingIds.pbkdf2, new Pbkdf2PasswordEncoder());
        encoders.put(EncodingIds.scrypt, new SCryptPasswordEncoder());
        encoders.put(EncodingIds.SHA_1, new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
        encoders.put(EncodingIds.SHA_256, new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
        encoders.put(EncodingIds.argon2, new Argon2PasswordEncoder());

        PasswordEncoder encoder = null;

        // MD2
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.MD2PasswordEncoder");
        if (encoder != null) {
            encoders.put(EncodingIds.MD2, encoder);
        }

        // SHA384
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.SHA384PasswordEncoder");
        if (encoder != null) {
            encoders.put(EncodingIds.SHA_384, encoder);
        }

        // SHA512
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.SHA512PasswordEncoder");
        if (encoder != null) {
            encoders.put(EncodingIds.SHA_512, encoder);
        }

        // SM3
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.SM3PasswordEncoder");
        if (encoder != null) {
            encoders.put(EncodingIds.SM3, encoder);
        }

        return encoders;
    }

    @Nullable
    private static PasswordEncoder getInstance(String classname) {
        try {
            final Optional<PasswordEncoder> oo = InstanceUtils.newInstance(classname);
            return oo.orElse(null);
        } catch (Throwable ignored) {
            return null;
        }
    }

}
