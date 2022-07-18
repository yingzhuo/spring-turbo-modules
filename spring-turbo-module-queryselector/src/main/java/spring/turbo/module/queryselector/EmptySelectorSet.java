/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author 应卓
 * @since 1.1.0
 */
public final class EmptySelectorSet implements SelectorSet {

    public static final EmptySelectorSet INSTANCE = new EmptySelectorSet();

    /**
     * 私有构造方法
     */
    private EmptySelectorSet() {
        super();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public List<Selector> toList() {
        return Collections.emptyList();
    }

    @Override
    public Iterator<Selector> iterator() {
        return Collections.<Selector>emptyList().listIterator();
    }

}
