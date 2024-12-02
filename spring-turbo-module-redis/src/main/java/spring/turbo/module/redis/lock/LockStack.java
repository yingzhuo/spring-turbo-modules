package spring.turbo.module.redis.lock;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 锁栈
 *
 * @author 应卓
 * @since 3.4.0
 */
final class LockStack implements Serializable {

    private final Stack<LockFrame> frames = new Stack<>();

    /**
     * 默认构造方法
     */
    public LockStack() {
        super();
    }

    @Nullable
    public LockFrame pop() {
        try {
            return frames.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public void push(LockFrame lockFrame) {
        frames.push(lockFrame);
    }

    @Nullable
    public LockFrame peek() {
        try {
            return frames.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public boolean isEmpty() {
        return frames.isEmpty();
    }

}
