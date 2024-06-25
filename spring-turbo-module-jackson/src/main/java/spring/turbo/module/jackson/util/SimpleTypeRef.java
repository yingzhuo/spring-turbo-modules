/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.util;

import com.jayway.jsonpath.TypeRef;

import java.lang.reflect.Type;

/**
 * 简单{@link TypeRef}实现
 *
 * @param <T> 泛型类型
 * @author 应卓
 * @since 3.3.2
 */
public final class SimpleTypeRef<T> extends TypeRef<T> {

    private final Class<T> clz;

    public SimpleTypeRef(Class<T> clz) {
        super();
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
