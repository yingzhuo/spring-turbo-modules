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
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.convention.ExtraPasswordEncoderConvention;
import spring.turbo.core.SpringFactoriesUtils;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    private static Map<String, PasswordEncoder> getEncoders() {
        var map = new HashMap<String, PasswordEncoder>();
        var services = SpringFactoriesUtils.loadQuietly(ExtraPasswordEncoderConvention.class);
        for (final var service : services) {
            CollectionUtils.nullSafeAddAll(map, service.getExtraPasswordEncoderWithName());
        }
        return Collections.unmodifiableMap(map);
    }

}
