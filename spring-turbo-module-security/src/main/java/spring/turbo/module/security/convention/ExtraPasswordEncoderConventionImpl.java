/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.convention;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import spring.turbo.convention.ExtraPasswordEncoderConvention;
import spring.turbo.module.security.encoder.EncodingIds;
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
        return HIGHEST_PRECEDENCE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Map<String, PasswordEncoder> getExtraPasswordEncoderWithName() {
        var map = new HashMap<String, PasswordEncoder>();
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

        PasswordEncoder encoder;

        // MD2
        encoder = loadInstance("spring.turbo.module.security.encoder.hutool.MD2PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.MD2, encoder);
        }

        // SHA384
        encoder = loadInstance("spring.turbo.module.security.encoder.hutool.SHA384PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.SHA_384, encoder);
        }

        // SHA512
        encoder = loadInstance("spring.turbo.module.security.encoder.hutool.SHA512PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.SHA_512, encoder);
        }

        // SM3
        encoder = loadInstance("spring.turbo.module.security.encoder.hutool.SM3PasswordEncoder");
        if (encoder != null) {
            map.put(EncodingIds.SM3, encoder);
        }

        return Collections.unmodifiableMap(map);
    }

    @Nullable
    private PasswordEncoder loadInstance(String classname) {
        try {
            Optional<PasswordEncoder> option = InstanceUtils.newInstance(classname);
            return option.orElse(null);
        } catch (Throwable ignored) {
            // 加载失败的主要原因:
            // 1. 没有默认构造方法
            // 2. 缺少依赖
            return null;
        }
    }

}
