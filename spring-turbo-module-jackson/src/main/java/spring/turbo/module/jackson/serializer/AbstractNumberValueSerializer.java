/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.serializer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import spring.turbo.util.Asserts;

/**
 * (内部使用)
 *
 * @param <T> 数值类型
 * @author 应卓
 * @see JsonSerialize#nullsUsing()
 */
abstract class AbstractNumberValueSerializer<T extends Number> extends JsonSerializer<T> {

    protected final T valueIfNull;

    public AbstractNumberValueSerializer(T valueIfNull) {
        Asserts.notNull(valueIfNull);
        this.valueIfNull = valueIfNull;
    }

}
