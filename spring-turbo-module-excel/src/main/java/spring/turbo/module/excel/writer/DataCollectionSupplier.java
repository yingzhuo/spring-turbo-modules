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
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.util.Collection;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.7
 */
@SuppressWarnings("unchecked")
public interface DataCollectionSupplier<T> {

    /**
     * 默认实现
     *
     * @param valueObjectType ValueObject类型
     * @param modelKey        Model Map 的键
     * @param <T>             ValueObject类型泛型
     * @return 实现类
     */
    public static <T> DataCollectionSupplier<T> getDefault(@NonNull final Class<T> valueObjectType, @NonNull final String modelKey) {
        Asserts.notNull(valueObjectType);
        Asserts.hasText(modelKey);
        return (model, type) -> (Collection<T>) model.get(modelKey);
    }

    @Nullable
    public Collection<T> apply(Map<String, Object> model, Class<T> valueObjectType);

}
