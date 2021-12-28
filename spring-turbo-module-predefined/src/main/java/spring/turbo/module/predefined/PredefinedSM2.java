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

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author 应卓
 * @since 1.0.2
 */
public final class PredefinedSM2 {

    private PredefinedSM2() {
        super();
    }

    public static Pair<String, String> loadKeys(int i) {
        Asserts.isTrue(i >= 0 && i <= 9, "i must be in range [0,10)");

        final String a = ResourceOptions
                .fromCommaSeparatedLocations(String.format("classpath:sm2/sm2.public.%d.txt", i)).toString(UTF_8);
        final String b = ResourceOptions
                .fromCommaSeparatedLocations(String.format("classpath:sm2/sm2.private.%d.txt", i)).toString(UTF_8);

        return Pair.of(a, b);
    }

}
