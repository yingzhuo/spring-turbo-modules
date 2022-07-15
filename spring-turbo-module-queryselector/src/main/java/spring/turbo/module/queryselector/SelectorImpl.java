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
import spring.turbo.bean.Pair;
import spring.turbo.lang.Immutable;
import spring.turbo.util.Asserts;

import java.util.Set;

/**
 * @author 应卓
 * @since 1.1.0
 */
@Immutable
@SuppressWarnings("unchecked")
public class SelectorImpl implements Selector {

    private final Item item;

    private final LogicType logicType;

    private final DataType dataType;

    @Nullable
    private final Object simpleValue;

    @Nullable
    private final Object rangeLeft;

    @Nullable
    private final Object rangeRight;

    @Nullable
    private final Set<Object> set;

    public SelectorImpl(String name, LogicType logicType, DataType dataType, @Nullable Object simpleValue) {
        this(name, logicType, dataType, simpleValue, null, null, null);
    }

    public SelectorImpl(String name, LogicType logicType, DataType dataType, @Nullable Object simpleValue, @Nullable Object rangeLeft, @Nullable Object rangeRight, @Nullable Set<Object> set) {
        Asserts.notNull(name);
        Asserts.notNull(logicType);
        Asserts.notNull(dataType);

        this.item = Item.of(name);
        this.logicType = logicType;
        this.dataType = dataType;
        this.simpleValue = simpleValue;
        this.rangeLeft = rangeLeft;
        this.rangeRight = rangeRight;
        this.set = set;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public LogicType getLogicType() {
        return this.logicType;
    }

    @Override
    public DataType getDataType() {
        return this.dataType;
    }

    @Nullable
    @Override
    public <T> T getSimpleValue() {
        return (T) this.simpleValue;
    }

    @Nullable
    @Override
    public <T> Pair<T, T> getValueRange() {
        return this.rangeLeft == null || this.rangeRight == null ?
                null :
                Pair.ofNonNull((T) this.rangeLeft, (T) this.rangeRight);
    }

    @Override
    @Nullable
    public <T> Set<T> getValueSet() {
        return (Set<T>) this.set;
    }

}
