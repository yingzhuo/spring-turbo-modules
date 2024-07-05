package spring.turbo.module.security.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import spring.turbo.util.Asserts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static cn.hutool.core.util.ReflectUtil.newInstance;
import static spring.turbo.util.StringUtils.isNotBlank;

/**
 * {@link PasswordEncoder} 创建工具
 *
 * @author 应卓
 * @see PasswordEncoder
 * @see DelegatingPasswordEncoder
 * @see EncodingIds
 * @since 1.0.0
 */
public final class PasswordEncoderFactories {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncoderFactories.class);

    /**
     * 私有构造方法
     */
    private PasswordEncoderFactories() {
    }

    public static BCryptPasswordEncoder createBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder() {
        return createDelegatingPasswordEncoder(EncodingIds.bcrypt, EncodingIds.noop);
    }

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(String encodingId) {
        return createDelegatingPasswordEncoder(encodingId, EncodingIds.noop);
    }

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(
            String encodingId,
            @Nullable String defaultPasswordEncoderForMatches) {

        Asserts.hasText(encodingId);

        var encodersMap = getEncoders();
        var ret = new DelegatingPasswordEncoder(encodingId, encodersMap);

        if (log.isInfoEnabled()) {
            var ids = encodersMap.keySet();
            log.info("supported encoder ids: [{}]", String.join(",", ids));
        }

        if (isNotBlank(defaultPasswordEncoderForMatches)) {
            ret.setDefaultPasswordEncoderForMatches(encodersMap.get(defaultPasswordEncoderForMatches));
        }

        return ret;
    }

    @SuppressWarnings("deprecation")
    private static Map<String, PasswordEncoder> getEncoders() {
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
    private static PasswordEncoder loadInstance(String classname) {
        try {
            return newInstance(classname);
        } catch (Throwable ignored) {
            // 加载失败的主要原因:
            // 1. 没有默认构造方法
            // 2. 缺少依赖
            log.debug("PasswordEncoding loading NG: '{}'", classname);
            return null;
        }
    }

}
