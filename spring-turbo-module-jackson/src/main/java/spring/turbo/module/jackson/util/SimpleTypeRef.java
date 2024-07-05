package spring.turbo.module.jackson.util;

import com.jayway.jsonpath.TypeRef;

import java.lang.reflect.Type;

/**
 * 简单{@link TypeRef}实现
 *
 * @param <T> 泛型类型
 * @author 应卓
 * @since 3.3.1
 */
public final class SimpleTypeRef<T> extends TypeRef<T> {

    private final Class<T> clz;

    public SimpleTypeRef(Class<T> clz) {

        this.clz = clz;
    }

    public static <T> TypeRef<T> of(Class<T> clazz) {
        return new SimpleTypeRef<>(clazz);
    }

    @Override
    public Type getType() {
        return this.clz;
    }

}
