/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.annotation;

import org.springframework.web.client.RestClient;
import spring.turbo.module.webcli.error.NoopResponseErrorHandler;

import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 3.3.1
 */
@FunctionalInterface
public interface RestClientSupplier extends Supplier<RestClient> {

    @Override
    public RestClient get();

    // -----------------------------------------------------------------------------------------------------------------

    public static class Default implements RestClientSupplier {
        public RestClient get() {
            return RestClient.builder()
                    .defaultStatusHandler(NoopResponseErrorHandler.getInstance())
                    .build();
        }
    }

}
