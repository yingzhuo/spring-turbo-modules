package spring.turbo.module.redis.lock;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Stack;

/**
 * 锁栈
 *
 * @author 应卓
 * @see ReentrantLock
 * @since 3.4.0
 */
public final class LockStack implements Serializable {

    private final Stack<LockFrame> frames = new Stack<>();

    /**
     * 默认构造方法
     */
    public LockStack() {
        super();
    }

    public void push(LockFrame lockFrame) {
        frames.push(lockFrame);
    }

    @Nullable
    public LockFrame pop() {
        return isEmpty() ? null : frames.pop();
    }

    @Nullable
    public LockFrame peek() {
        return isEmpty() ? null : frames.peek();
    }

    public boolean isEmpty() {
        return frames.isEmpty();
    }

}
