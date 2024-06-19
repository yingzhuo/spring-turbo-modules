/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.support;

import org.springframework.util.StringValueResolver;
import spring.turbo.core.EnvironmentUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 3.1.1
 */
public interface EmbeddedValueResolverSupplier extends Supplier<StringValueResolver> {

    @Nullable
    @Override
    default StringValueResolver get() {
        return strVal -> {
            try {
                return EnvironmentUtils.resolvePlaceholders(strVal);
            } catch (Exception e) {
                return strVal;
            }
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    class Default implements EmbeddedValueResolverSupplier {
    }

}
