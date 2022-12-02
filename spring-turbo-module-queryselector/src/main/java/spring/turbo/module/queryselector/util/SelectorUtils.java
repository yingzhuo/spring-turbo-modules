/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.util;

import org.springframework.lang.Nullable;
import spring.turbo.bean.Pair;
import spring.turbo.module.queryselector.DataType;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.exception.SelectorValueFindingException;
import spring.turbo.util.BigDecimalUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 应卓
 * @since 2.0.1
 */
@SuppressWarnings("unchecked")
public final class SelectorUtils {

    /**
     * 私有构造方法
     */
    private SelectorUtils() {
        super();
    }

    public static <T> T getSimpleValueAndConvert(Selector selector, Class<T> targetClass) {
        final Object object = selector.getSimpleValue();
        return doConvert(object, selector.getDataType(), targetClass);
    }

    public static <T> Set<T> getValueSetAndConvert(Selector selector, Class<T> targetClass) {
        final Set<Object> set = selector.getSetValue();

        if (set == null) {
            throw new SelectorValueFindingException("Cannot get simple value");
        }

        final Set<T> newSet = new HashSet<>();
        for (Object value : set) {
            newSet.add(doConvert(value, selector.getDataType(), targetClass));
        }
        return newSet;
    }

    public static <T> Pair<T, T> getValueRangeAndConvert(Selector selector, Class<T> targetClass) {
        final Pair<Object, Object> pair = selector.getRangeValue();

        if (pair == null) {
            throw new SelectorValueFindingException("Cannot get simple value");
        }

        return Pair.ofNonNull(
                doConvert(pair.getRequiredA(), selector.getDataType(), targetClass),
                doConvert(pair.getRequiredB(), selector.getDataType(), targetClass)
        );
    }

    private static <T> T doConvert(@Nullable final Object value, DataType dataType, Class<?> targetClass) {
        if (value == null) {
            throw new SelectorValueFindingException("Cannot get simple value");
        }

        if (dataType == DataType.STRING && targetClass == String.class) {
            return (T) value;
        }

        if (dataType == DataType.NUMBER) {
            if (targetClass == Byte.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, Byte.class);
            }
            if (targetClass == Short.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, Short.class);
            }
            if (targetClass == Integer.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, Integer.class);
            }
            if (targetClass == Long.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, Long.class);
            }
            if (targetClass == Float.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, Float.class);
            }
            if (targetClass == Double.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, Double.class);
            }
            if (targetClass == BigInteger.class) {
                return (T) BigDecimalUtils.getValue((BigDecimal) value, BigInteger.class);
            }
            if (targetClass == BigDecimal.class) {
                return (T) value;
            }
        }

        if ((dataType == DataType.DATE || dataType == DataType.DATETIME) && targetClass == Date.class) {
            return (T) value;
        }

        throw new SelectorValueFindingException("Cannot get simple value");
    }
}
