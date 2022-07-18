/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import spring.turbo.util.Asserts;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author 应卓
 * @since 1.1.0
 */
public class SelectorSetImpl implements SelectorSet {

    private final List<Selector> selectors;

    public SelectorSetImpl(List<Selector> selectors) {
        Asserts.notNull(selectors);
        this.selectors = selectors;
    }

    @Override
    public int size() {
        return selectors.size();
    }

    @Override
    public Iterator<Selector> iterator() {
        return selectors.listIterator();
    }

    @Override
    public List<Selector> asList() {
        return Collections.unmodifiableList(selectors);
    }

}
