/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.convention.spi;

import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import spring.turbo.convention.ExtraPasswordEncoderConvention;
import spring.turbo.module.security.encoder.EncodingIds;
import spring.turbo.module.security.encoder.HEXPasswordEncoder;
import spring.turbo.util.InstanceUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link ExtraPasswordEncoderConvention} 实现
 *
 * @author 应卓
 * @see DelegatingPasswordEncoder
 * @since 2.0.3
 */
public final class ExtraPasswordEncoderConventionImpl implements ExtraPasswordEncoderConvention {

    /**
     * 默认构造方法
     */
    public ExtraPasswordEncoderConventionImpl() {
        super();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Map<String, PasswordEncoder> getExtraPasswordEncoderWithName() {
        final var map = new HashMap<String, PasswordEncoder>();
        map.put(EncodingIds.bcrypt, new BCryptPasswordEncoder());
        map.put(EncodingIds.noop, NoOpPasswordEncoder.getInstance());
        map.put(EncodingIds.ldap, new LdapShaPasswordEncoder());
        map.put(EncodingIds.MD4, new Md4PasswordEncoder());
        map.put(EncodingIds.MD5, new MessageDigestPasswordEncoder("MD5"));
        map.put(EncodingIds.SHA_1, new MessageDigestPasswordEncoder("SHA-1"));
        map.put(EncodingIds.SHA_256, new MessageDigestPasswordEncoder("SHA-256"));
        map.put(EncodingIds.pbkdf2, Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        map.put(EncodingIds.scrypt, SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        map.put(EncodingIds.argon2, Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        map.put(EncodingIds.HEX, HEXPasswordEncoder.getInstance());

        PasswordEncoder encoder;

        // MD2
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.MD2PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.MD2, encoder);
        }

        // SHA384
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.SHA384PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.SHA_384, encoder);
        }

        // SHA512
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.SHA512PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.SHA_512, encoder);
        }

        // SM3
        encoder = getInstance("spring.turbo.module.security.encoder.hutool.SM3PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.SM3, encoder);
        }

        return Collections.unmodifiableMap(map);
    }

    @Nullable
    private PasswordEncoder getInstance(String classname) {
        try {
            final Optional<PasswordEncoder> oo = InstanceUtils.newInstance(classname);
            return oo.orElse(null);
        } catch (Throwable ignored) {
            return null;
        }
    }

}
