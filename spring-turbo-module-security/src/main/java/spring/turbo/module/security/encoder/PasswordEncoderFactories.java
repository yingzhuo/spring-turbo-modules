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
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.util.Asserts;
import spring.turbo.util.ServiceLoaderUtils;
import spring.turbo.util.StringUtils;

import java.util.*;

/**
 * @author 应卓
 * @see EncodingIds
 * @since 1.0.0
 */
public final class PasswordEncoderFactories {

    private PasswordEncoderFactories() {
        super();
    }

    @NonNull
    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder() {
        return createDelegatingPasswordEncoder(EncodingIds.bcrypt, EncodingIds.noop);
    }

    @NonNull
    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(@NonNull String encodingId) {
        return createDelegatingPasswordEncoder(encodingId, EncodingIds.noop);
    }

    @NonNull
    public static DelegatingPasswordEncoder createDelegatingPasswordEncoder(@NonNull String encodingId, @Nullable String defaultPasswordEncoderForMatches) {
        Asserts.hasText(encodingId);
        final Map<String, PasswordEncoder> encoders = loadFromModules();
        final DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(encodingId, encoders);

        if (StringUtils.isNotBlank(defaultPasswordEncoderForMatches)) {
            encoder.setDefaultPasswordEncoderForMatches(encoders.get(defaultPasswordEncoderForMatches));
        }

        return encoder;
    }

    // since 1.0.2
    private static Map<String, PasswordEncoder> loadFromModules() {
        final Map<String, PasswordEncoder> map = new HashMap<>();
        final List<NamedPasswordEncoderProvider> providers = ServiceLoaderUtils.loadQuietly(NamedPasswordEncoderProvider.class);

        for (NamedPasswordEncoderProvider provider : providers) {
            final Collection<NamedPasswordEncoder> encoders = provider.getPasswordEncoders();
            if (encoders != null) {
                for (NamedPasswordEncoder encoder : encoders) {
                    if (encoder != null) {
                        map.put(encoder.getName(), encoder);
                    }
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

}
