package spring.turbo.module.redis.lock;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * 分布式锁的实现
 *
 * @author 应卓
 * @see LockStamp
 * @see ValueGeneratingStrategy
 * @since 3.4.0
 */
public final class ReentrantLock extends AbstractLock {

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     */
    public ReentrantLock(RedisOperations<String, String> redisOperations) {
        super(redisOperations, null);
    }

    /**
     * 构造方法
     *
     * @param redisOperations         RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param valueGeneratingStrategy value生成策略
     */
    public ReentrantLock(RedisOperations<String, String> redisOperations, @Nullable ValueGeneratingStrategy valueGeneratingStrategy) {
        super(redisOperations, valueGeneratingStrategy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LockStamp lock(String lockKey, Duration lockTimeToLive) {
        Assert.hasText(lockKey, "localKey is null or blank");
        Assert.notNull(lockTimeToLive, "lockTimeToLive is null");

        var lockField = getLockField(lockKey);

        var lua = """
                if redis.call('EXISTS', ARGV[1]) == 1 then
                    if redis.call('HEXISTS', ARGV[1], ARGV[2]) == 0 then
                        return 0
                    end
                end
                                
                local n = redis.call('HINCRBY', ARGV[1], ARGV[2], 1)
                redis.call('EXPIRE', ARGV[1], ARGV[3])
                return n > 0
                """;

        var success = redisOperations.execute(
                RedisScript.of(lua, Boolean.class),
                List.of(),
                lockKey,
                lockField,
                String.valueOf(lockTimeToLive.toSeconds())
        );

        var stamp = new LockStamp(success, lockKey, lockField, System.currentTimeMillis() + lockTimeToLive.toMillis());
        if (stamp.isSuccess()) {
            super.lastSuccessLockStamp = stamp;
        }
        return stamp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unlock() {
        var last = super.lastSuccessLockStamp;
        if (last == null) {
            return false;
        }

        var lockKey = last.getLockKey();
        var lockField = last.getLockValue();

        var lua = """
                if redis.call('EXISTS', ARGV[1]) == 1 then
                    if redis.call('HEXISTS', ARGV[1], ARGV[2]) == 0 then
                        return false
                    end
                end
                                
                local n = redis.call('HINCRBY', ARGV[1], ARGV[2], -1)
                if n <= 0 then
                    redis.call('DEL', ARGV[1])
                end
                       
                return true
                """;

        return redisOperations.execute(
                RedisScript.of(lua, Boolean.class),
                List.of(),
                lockKey,
                lockField
        );
    }

    private String getLockField(String lockKey) {
        return Optional.ofNullable(getLastSuccessLockStamp())
                .map(LockStamp::getLockValue)
                .orElse(valueGeneratingStrategy.apply(lockKey));
    }

}
