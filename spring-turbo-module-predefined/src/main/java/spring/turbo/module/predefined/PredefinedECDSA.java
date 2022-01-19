/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.predefined;

import spring.turbo.bean.Pair;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.ECDSA;
import spring.turbo.util.crypto.ECDSAKeys;

import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class PredefinedECDSA {

    private PredefinedECDSA() {
        super();
    }

    public static Pair<String, String> loadKeys(int i) {
        Asserts.isTrue(i >= 0 && i <= 9, "i must be in range [0,10)");

        final String a = ResourceOptions
                .fromSeparatedLocations(String.format("classpath:ecdsa/ecdsa.public.%d.txt", i)).toString(UTF_8);
        final String b = ResourceOptions
                .fromSeparatedLocations(String.format("classpath:ecdsa/ecdsa.private.%d.txt", i)).toString(UTF_8);

        return Pair.of(a, b);
    }

    public static ECDSA createCrypto(int i) {
        final Pair<String, String> ks = loadKeys(i);
        return ECDSA.builder()
                .keyPair(
                        ECDSAKeys.fromString(
                                Objects.requireNonNull(ks.getA()),
                                Objects.requireNonNull(ks.getB())
                        )
                )
                .build();
    }

}
