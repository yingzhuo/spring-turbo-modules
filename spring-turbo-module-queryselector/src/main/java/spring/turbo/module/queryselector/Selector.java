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
import spring.turbo.module.queryselector.util.SelectorUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * @author 应卓
 * @see SelectorUtils
 * @see SelectorBuilder
 * @see SelectorBuilder#newInstance()
 * @see #builder()
 * @since 1.1.0
 */
public interface Selector extends Serializable {

    public static SelectorBuilder builder() {
        return SelectorBuilder.newInstance();
    }

    public Item getItem();

    public LogicType getLogicType();

    public DataType getDataType();

    @Nullable
    public <T> T getSimpleValue();

    @Nullable
    public <T> Pair<T, T> getRangeValue();

    @Nullable
    public <T> Set<T> getSetValue();

}
