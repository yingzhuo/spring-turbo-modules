/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.writer;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author 应卓
 * @since 1.0.6
 */
@FunctionalInterface
@SuppressWarnings("unchecked")
public interface ValueObjectCollectionProvider<T> extends BiFunction<Map<String, Object>, Class<T>, Collection<T>> {

    @NonNull
    public static <T> ValueObjectCollectionProvider<T> modelsKey(@NonNull String key) {
        return new ModelsKeyValueObjectCollectionProvider(key);
    }

    @Override
    public Collection<T> apply(Map<String, Object> models, Class<T> valueObjectType);

}
