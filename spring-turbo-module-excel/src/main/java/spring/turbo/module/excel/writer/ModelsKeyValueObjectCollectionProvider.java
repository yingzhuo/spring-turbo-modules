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
import spring.turbo.util.Asserts;

import java.util.Collection;
import java.util.Map;

/**
 * (内部工具)
 *
 * @author 应卓
 * @since 1.0.6
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class ModelsKeyValueObjectCollectionProvider<T> implements ValueObjectCollectionProvider<T> {

    private final String key;

    public ModelsKeyValueObjectCollectionProvider(@NonNull String key) {
        Asserts.hasText(key);
        this.key = key;
    }

    @Override
    public Collection<T> apply(Map<String, Object> models, Class<T> tClass) {
        return (Collection<T>) models.get(key);
    }

}
