/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Selector> iterator() {
        return new Iterator<Selector>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Selector next() {
                throw new NoSuchElementException();
            }
        };
    }

}
