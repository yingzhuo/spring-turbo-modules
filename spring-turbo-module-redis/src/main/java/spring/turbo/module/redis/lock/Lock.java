package spring.turbo.module.redis.lock;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import spring.turbo.util.StringPool;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * 分布式锁的简易实现 <br>
 * <em>警告: 只适合小微企业使用</em>
 *
 * @author 应卓
 * @see LockStamp
 * @see ValueGeneratingStrategy
 * @since 3.4.0
 */
public final class Lock implements Serializable {

    @NonNull
    private final RedisOperations<String, String> redisOperations;

    @NonNull
    private final ValueGeneratingStrategy valueGeneratingStrategy;

    @Nullable
    private LockStamp lastSuccessLockStamp = null;

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     */
    public Lock(RedisOperations<String, String> redisOperations) {
        this(redisOperations, null);
    }

    /**
     * 构造方法
     *
     * @param redisOperations         RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param valueGeneratingStrategy value生成策略
     */
    public Lock(RedisOperations<String, String> redisOperations, @Nullable ValueGeneratingStrategy valueGeneratingStrategy) {
        Assert.notNull(redisOperations, "redisOperations is null");

        this.redisOperations = redisOperations;
        this.valueGeneratingStrategy = Objects.requireNonNullElseGet(valueGeneratingStrategy, ValueGeneratingStrategy::defaultInstance);
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

    /**
     * 获取最后一次成功时的戳记
     *
     * @return 戳记实例
     */
    @Nullable
    public LockStamp getLastSuccessLockStamp() {
        return lastSuccessLockStamp;
    }

}
