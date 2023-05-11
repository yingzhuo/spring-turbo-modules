/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.convention.ExtraPasswordEncoderConvention;
import spring.turbo.util.Asserts;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static spring.turbo.core.SpringFactoriesUtils.loadQuietly;
import static spring.turbo.util.CollectionUtils.nullSafeAddAll;
import static spring.turbo.util.StringUtils.isNotBlank;

/**
 * @author 应卓
 *
 * @see PasswordEncoder
 * @see DelegatingPasswordEncoder
 * @see EncodingIds
 *
 * @since 1.0.0
 */
public final class PasswordEncoderFactories {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncoderFactories.class);

    /**
     * 私有构造方法
     */
    private PasswordEncoderFactories() {
        super();
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

    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(String encodingId,
            @Nullable String defaultPasswordEncoderForMatches) {
        Asserts.hasText(encodingId);

        var encodersMap = getEncoders();
        var ret = new DelegatingPasswordEncoder(encodingId, encodersMap);

        if (log.isInfoEnabled()) {
            var ids = encodersMap.keySet().stream().filter(s -> !"broken".equals(s)).toList();
            log.info("supported encoder ids: [{}]", String.join(",", ids));
        }

        if (isNotBlank(defaultPasswordEncoderForMatches)) {
            ret.setDefaultPasswordEncoderForMatches(encodersMap.get(defaultPasswordEncoderForMatches));
        }

        return ret;
    }

    private static Map<String, PasswordEncoder> getEncoders() {
        var map = new TreeMap<String, PasswordEncoder>();
        // spi加载
        var services = loadQuietly(ExtraPasswordEncoderConvention.class);
        for (var service : services) {
            try {
                nullSafeAddAll(map, service.getExtraPasswordEncoderWithName());
            } catch (Throwable ignored) {
                // 无动作
            }
        }
        return Collections.unmodifiableMap(map);
    }

}
