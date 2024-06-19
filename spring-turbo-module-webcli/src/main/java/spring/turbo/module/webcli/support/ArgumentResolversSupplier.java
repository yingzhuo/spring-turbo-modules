/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.support;

import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 3.3.1
 */
public interface ArgumentResolversSupplier extends Supplier<Collection<HttpServiceArgumentResolver>> {

    @Nullable
    @Override
    public Collection<HttpServiceArgumentResolver> get();

    // -----------------------------------------------------------------------------------------------------------------

    public static class Default implements ArgumentResolversSupplier {
        public Collection<HttpServiceArgumentResolver> get() {
            return null;
        }
    }

}
