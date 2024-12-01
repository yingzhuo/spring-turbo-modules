package spring.turbo.module.redis.lock;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import spring.turbo.util.StringPool;

import java.time.Duration;
import java.util.List;

/**
 * 分布式锁的简易实现 <br>
 * <em>警告: 这不是可重入锁，只适合小微企业使用。</em>
 *
 * @author 应卓
 * @see ReentrantLock
 * @see LockStamp
 * @see ValueGeneratingStrategy
 * @since 3.4.0
 * @deprecated 此锁不可重入，不怎么靠谱
 */
@Deprecated
public final class NonReentrantLock extends AbstractLock {

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     */
    public NonReentrantLock(RedisOperations<String, String> redisOperations) {
        super(redisOperations, null);
    }

    /**
     * 构造方法
     *
     * @param redisOperations         RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param valueGeneratingStrategy value生成策略
     */
    public NonReentrantLock(RedisOperations<String, String> redisOperations, @Nullable ValueGeneratingStrategy valueGeneratingStrategy) {
        super(redisOperations, valueGeneratingStrategy);
    }

    /**
     * 加锁
     *
     * @param lockKey        键
     * @param lockTimeToLive ttl
     * @return 加锁戳记
     * @see LockStamp#isSuccess()
     */
    public LockStamp lock(String lockKey, Duration lockTimeToLive) {
        Assert.hasText(lockKey, "localKey is null or blank");
        Assert.notNull(lockTimeToLive, "lockTimeToLive is null");

        var lockValue = valueGeneratingStrategy.apply(lockKey);
        var success = redisOperations.opsForValue().setIfAbsent(lockKey, lockValue, lockTimeToLive);
        var stamp = new LockStamp(success, lockKey, lockValue, System.currentTimeMillis() + lockTimeToLive.toMillis());
        if (stamp.isSuccess()) {
            this.lastSuccessLockStamp = stamp;
        }
        return stamp;
    }

    /**
     * 解锁
     *
     * @return 返回true时表示成功解锁
     */
    public boolean unlock() {
        return doUnlock(getLastSuccessLockStamp());
    }

    /**
     * 解锁
     *
     * @param lockStamp 加锁戳记
     * @return 返回true时表示成功解锁
     */
    private boolean doUnlock(@Nullable LockStamp lockStamp) {
        if (lockStamp == null) {
            return false;
        }

        var lua = """
                if redis.call('GET', ARGV[1]) == ARGV[2] then
                    redis.call('DEL', ARGV[1]);
                    return 'OK'
                else
                    return 'NG'
                end
                """;

        var ret = redisOperations.execute(RedisScript.of(lua, String.class), List.of(), lockStamp.getLockKey(), lockStamp.getLockValue());
        return StringPool.isOK(ret);
    }

}
