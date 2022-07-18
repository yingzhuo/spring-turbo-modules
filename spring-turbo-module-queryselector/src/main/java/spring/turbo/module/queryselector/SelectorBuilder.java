/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.1.2
 */
public final class SelectorBuilder {

    @Nullable
    private String itemName;

    @Nullable
    private DataType dataType;

    @Nullable
    private LogicType logicType;

    @Nullable
    private Object simpleValue;

    @Nullable
    private Object rangeLeft;

    @Nullable
    private Object rangeRight;

    @NonNull
    private final Set<Object> setValue = new HashSet<>();

    public static SelectorBuilder newInstance() {
        return new SelectorBuilder();
    }

    /**
     * 私有构造方法
     */
    private SelectorBuilder() {
        super();
    }

    public SelectorBuilder itemName(String itemName) {
        Asserts.hasText(itemName);
        this.itemName = itemName;
        return this;
    }

    public SelectorBuilder logicType(LogicType logicType) {
        Asserts.notNull(logicType);
        this.logicType = logicType;
        return this;
    }

    public SelectorBuilder dataType(DataType dataType) {
        Asserts.notNull(dataType);
        this.dataType = dataType;
        return this;
    }

    public SelectorBuilder simpleValue(Object value) {
        Asserts.notNull(value);
        this.simpleValue = value;
        return this;
    }

    public SelectorBuilder rangeValue(Object left, Object right) {
        Asserts.notNull(left);
        Asserts.notNull(right);
        this.rangeLeft = left;
        this.rangeRight = right;
        return this;
    }

    public SelectorBuilder setValue(Object... values) {
        CollectionUtils.nullSafeAddAll(this.setValue, values);
        return this;
    }

    public Selector build() {
        Asserts.notNull(itemName);
        Asserts.notNull(logicType);
        Asserts.notNull(dataType);

        return new SelectorImpl(
                this.itemName,
                this.logicType,
                this.dataType,
                this.simpleValue,
                this.rangeLeft,
                this.rangeRight,
                this.setValue
        );
    }
}
