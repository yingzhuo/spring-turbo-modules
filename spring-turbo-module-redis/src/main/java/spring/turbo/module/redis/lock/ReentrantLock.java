package spring.turbo.module.redis.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import spring.turbo.util.concurrent.CurrentThreadUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;

/**
 * 基于Redis的可重入分布式锁 <br>
 * <em>特色</em>
 * <ul>
 *     <li>可重入</li>
 *     <li>实现了自动续期功能</li>
 * </ul>
 *
 * @author 应卓
 * @since 3.4.0
 */
public final class ReentrantLock implements Serializable {

    /*
     * 本工具没有在高并发下严格测试，作者只为了自学与教学。请谨慎在生产环境上使用。
     */

    /**
     * 加锁脚本
     */
    private static final RedisScript<Long> TRY_LOCK =
            RedisScript.of(new ClassPathResource("META-INF/Lock#lock.lua"), Long.class);

    /**
     * 解锁脚本
     */
    private static final RedisScript<Boolean> UNLOCK =
            RedisScript.of(new ClassPathResource("META-INF/Lock#unlock.lua"), Boolean.class);

    private final RedisOperations<String, String> redisOperations;
    private final LockStack lockStack;
    private final long ttlInSeconds;
    private final String lockKey;

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param lockKey         作为锁的键
     * @param ttlInSeconds    锁自动过期时间(秒)
     */
    public ReentrantLock(RedisOperations<String, String> redisOperations, String lockKey, long ttlInSeconds) {
        Assert.notNull(redisOperations, "redisOperations is required");
        Assert.hasText(lockKey, "lockKey is required");
        Assert.isTrue(ttlInSeconds > 0, "ttlInSeconds must greater than 0");

        this.redisOperations = redisOperations;
        this.lockKey = lockKey;
        this.ttlInSeconds = ttlInSeconds;
        this.lockStack = new LockStack();
    }

    /**
     * 尝试加锁
     *
     * @return 加锁结果
     */
    public boolean tryLock() {
        var now = System.currentTimeMillis();
        var lockField = CurrentThreadUtils.getTrait();

        long reentrantCount = redisOperations.execute(
                TRY_LOCK,
                List.of(),
                lockKey,
                lockField,
                String.valueOf(ttlInSeconds)
        );

        if (reentrantCount >= 1) {
            // 栈桢入栈并返回
            var frame = new LockFrame(
                    now,
                    lockKey, lockField, ttlInSeconds,
                    reentrantCount,
                    CurrentThreadUtils.getId(), CurrentThreadUtils.getName()
            );
            lockStack.push(frame);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 尝试解锁
     *
     * @return 解锁结果
     */
    public boolean unlock() {
        var lockField = CurrentThreadUtils.getTrait();

        var success = redisOperations.execute(
                UNLOCK,
                List.of(),
                lockKey,
                lockField
        );

        if (success) {
            var frame = lockStack.peek();

            if (frame != null && frame.getLockField().equals(lockField)) {
                frame.getNullableTimer().ifPresent(Timer::cancel);
                lockStack.pop();
            }
        }

        return success;
    }

    /**
     * 开启后台线程在到期时间2/3时，重置TTL
     */
    public void renewTtl() {
        var timer = new Timer(true);
        var lockField = CurrentThreadUtils.getTrait();
        timer.schedule(new RenewTask(redisOperations, lockKey, lockField, ttlInSeconds), ttlInSeconds * 1000 * 2 / 3);

        var frame = lockStack.peek();
        frame.setTimer(timer);
    }

    /**
     * 获取当前重入锁的帧
     *
     * @return 当前重入锁的帧
     */
    @Nullable
    public LockFrame getCurrentFrame() {
        return lockStack.peek();
    }

}
