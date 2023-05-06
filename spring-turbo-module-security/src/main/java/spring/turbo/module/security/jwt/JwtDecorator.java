/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.json.JSONArray;
import cn.hutool.jwt.JWT;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.io.Serializable;

/**
 * JWT装饰器
 *
 * @param jwt
 *            被装饰的 {@link JWT} 实例
 *
 * @author 应卓
 *
 * @since 2.2.5
 */
public final record JwtDecorator(JWT jwt) implements Serializable {

    public static JwtDecorator newInstance(JWT jwt) {
        return new JwtDecorator(jwt);
    }

    @Nullable
    public String getPayloadString(String name) {
        try {
            return (String) jwt.getPayload(name);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public String getPayloadRequiredString(String name) {
        var value = getPayloadString(name);
        Asserts.notNull(value);
        return value;
    }

    @Nullable
    public Integer getPayloadInteger(String name) {
        try {
            return ((NumberWithFormat) jwt.getPayload(name)).intValue();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public Long getPayloadLong(String name) {
        return ((NumberWithFormat) jwt.getPayload(name)).longValue();
    }

    @NonNull
    public Long getPayloadRequiredLong(String name) {
        var value = getPayloadLong(name);
        Asserts.notNull(value);
        return value;
    }

    @Nullable
    public Double getPayloadDouble(String name) {
        try {
            return ((NumberWithFormat) jwt.getPayload(name)).doubleValue();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public Double getPayloadRequiredDouble(String name) {
        var value = getPayloadDouble(name);
        Asserts.notNull(value);
        return value;
    }

    @Nullable
    public String[] getPayloadStringArray(String name) {
        try {
            return (String[]) ((JSONArray) jwt.getPayload(name)).toArray(String[].class);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public String[] getPayloadRequiredStringArray(String name) {
        var array = getPayloadStringArray(name);
        Asserts.notNull(array);
        return array;
    }

    @Nullable
    public Integer[] getPayloadIntegerArray(String name) {
        try {
            return (Integer[]) ((JSONArray) jwt.getPayload(name)).toArray(Integer[].class);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public Integer[] getPayloadRequiredIntegerArray(String name) {
        var array = getPayloadIntegerArray(name);
        Asserts.notNull(array);
        return array;
    }

    @Nullable
    public Long[] getPayloadLongArray(String name) {
        try {
            return (Long[]) ((JSONArray) jwt.getPayload(name)).toArray(Long[].class);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public Long[] getPayloadRequiredLongArray(String name) {
        var array = getPayloadLongArray(name);
        Asserts.notNull(array);
        return array;
    }

    @Nullable
    public Double[] getPayloadDoubleArray(String name) {
        try {
            return (Double[]) ((JSONArray) jwt.getPayload(name)).toArray(Double[].class);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @NonNull
    public Double[] getPayloadRequiredDoubleArray(String name) {
        var array = getPayloadDoubleArray(name);
        Asserts.notNull(array);
        return array;
    }

}
