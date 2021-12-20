/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.batch;

import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 一个批次的数据 (单个线程有效)
 *
 * @param <T> ValueObject类型
 * @author 应卓
 * @since 1.0.0
 */
public class Batch<T> implements Iterable<T>, Serializable {

    private final int maxSize;
    private final ThreadLocal<List<T>> threadLocal;

    public Batch(final int maxSize) {
        Asserts.isTrue(maxSize > 0);
        this.maxSize = maxSize;
        this.threadLocal = ThreadLocal.withInitial(() -> new ArrayList<>(maxSize));
    }

    public void add(final T element) {
        if (isFull()) {
            throw new IllegalArgumentException("batch is full");
        }
        threadLocal.get().add(element);
    }

    public void addAll(final List<T> elements) {
        if (elements != null) {
            for (T element : elements) {
                add(element);
            }
        }
    }

    public int size() {
        return threadLocal.get().size();
    }

    public boolean isEmpty() {
        return threadLocal.get().isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public boolean isFull() {
        return size() >= maxSize;
    }

    public boolean isNotFull() {
        return !isFull();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void clear() {
        threadLocal.get().clear();
    }

    @Override
    public Iterator<T> iterator() {
        return threadLocal.get().iterator();
    }

    public Batch<T> filter(Predicate<T> predicate) {
        final List<T> xs = threadLocal.get().stream().filter(predicate).collect(Collectors.toList());
        final Batch<T> newBatch = new Batch<>(this.maxSize);
        newBatch.addAll(xs);
        return newBatch;
    }

    public <U> Batch<U> map(Function<T, U> function) {
        final List<U> xs = threadLocal.get().stream().map(function).collect(Collectors.toList());
        final Batch<U> newBatch = new Batch<>(this.maxSize);
        newBatch.addAll(xs);
        return newBatch;
    }

}
