/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import spring.turbo.util.collection.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * 选择器集合
 *
 * @author 应卓
 *
 * @since 2.0.1
 */
public interface SelectorSet extends Iterable<Selector>, Serializable {

    public static SelectorSet empty() {
        return EmptySelectorSet.getInstance();
    }

    public static SelectorSet of(Selector... selectors) {
        final List<Selector> selectorList = new ArrayList<>();
        CollectionUtils.nullSafeAddAll(selectorList, selectors);
        return new SelectorSetImpl(selectorList);
    }

    public static SelectorSet of(Collection<Selector> selectors) {
        final List<Selector> selectorList = new ArrayList<>();
        CollectionUtils.nullSafeAddAll(selectorList, selectors);
        return new SelectorSetImpl(selectorList);
    }

    public List<Selector> toList();

    public default Stream<Selector> toStream() {
        return toList().stream();
    }

    public default int size() {
        return toList().size();
    }

    public boolean isEmpty();

    @Override
    public String toString();

}
