/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import org.springframework.lang.Nullable;
import spring.turbo.bean.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.1.0
 */
public interface Selector extends Named, Serializable {

    @Override
    public String getName();

    public LogicType getLogicType();

    public DataType getDataType();

    @Nullable
    public <T> T getSimpleValue();

    @Nullable
    public default Integer getSimpleValueAsInteger() {
        try {
            final BigDecimal decimal = getSimpleValue();
            return decimal != null ? decimal.intValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default Long getSimpleValueAsLong() {
        try {
            final BigDecimal decimal = getSimpleValue();
            return decimal != null ? decimal.longValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default Double getSimpleValueAsDouble() {
        try {
            final BigDecimal decimal = getSimpleValue();
            return decimal != null ? decimal.doubleValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default BigInteger getSimpleValueAsBigInteger() {
        try {
            final BigDecimal decimal = getSimpleValue();
            return decimal != null ? decimal.toBigInteger() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default BigDecimal getSimpleValueAsBigDecimal() {
        return getSimpleValue();
    }

    @Nullable
    public <T> T getValueRangeLeft();

    @Nullable
    public default Integer getValueRangeLeftAsInteger() {
        try {
            final BigDecimal decimal = getValueRangeLeft();
            return decimal != null ? decimal.intValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default Long getValueRangeLeftAsLong() {
        try {
            final BigDecimal decimal = getValueRangeLeft();
            return decimal != null ? decimal.longValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default Double getValueRangeLeftAsDouble() {
        try {
            final BigDecimal decimal = getValueRangeLeft();
            return decimal != null ? decimal.doubleValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default BigInteger getValueRangeLeftAsBigInteger() {
        try {
            final BigDecimal decimal = getValueRangeLeft();
            return decimal != null ? decimal.toBigInteger() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default BigDecimal getValueRangeLeftAsBigDecimal() {
        return getValueRangeLeft();
    }

    @Nullable
    public <T> T getValueRangeRight();

    @Nullable
    public default Integer getValueRangeRightAsInteger() {
        try {
            final BigDecimal decimal = getValueRangeRight();
            return decimal != null ? decimal.intValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default Long getValueRangeRightAsLong() {
        try {
            final BigDecimal decimal = getValueRangeRight();
            return decimal != null ? decimal.longValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default Double getValueRangeRightAsDouble() {
        try {
            final BigDecimal decimal = getValueRangeRight();
            return decimal != null ? decimal.doubleValue() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default BigInteger getValueRangeRightAsBigInteger() {
        try {
            final BigDecimal decimal = getValueRangeRight();
            return decimal != null ? decimal.toBigInteger() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Nullable
    public default BigDecimal getValueRangeRightAsBigDecimal() {
        return getValueRangeRight();
    }

    @Nullable
    public <T> Set<T> getValueSet();

}
