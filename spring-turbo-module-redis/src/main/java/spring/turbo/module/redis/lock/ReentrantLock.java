package spring.turbo.module.redis.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

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
    public LockStamp lock(String lockKey, long ttlInSeconds) {
        Assert.hasText(lockKey, "localKey is null or blank");
        Assert.isTrue(ttlInSeconds > 0, "ttlInSeconds should > 0");

        var field = getLockField(lockKey);

        var success = redisOperations.execute(
                SyncAvoid.LUA_LOCK,
                List.of(),
                lockKey,
                field,
                String.valueOf(ttlInSeconds)
        );

        var stamp = new LockStamp(success, lockKey, field, System.currentTimeMillis() + ttlInSeconds * 1000, ttlInSeconds);
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

        var key = last.getLockKey();
        var field = last.getLockValue();

        return redisOperations.execute(
                SyncAvoid.LUA_UNLOCK,
                List.of(),
                key,
                field
        );
    }

    private String getLockField(String lockKey) {
        return Optional.ofNullable(getLastSuccessLockStamp())
                .map(LockStamp::getLockValue)
                .orElse(valueGeneratingStrategy.apply(lockKey));
    }

    // 延迟加载
    private static class SyncAvoid {

        /**
         * 加锁LUA脚本
         */
        private static final RedisScript<Boolean> LUA_LOCK =
                RedisScript.of(new ClassPathResource("META-INF/script/spring.turbo.module.redis.lock.ReentrantLock#lock.lua"), Boolean.class);

        /**
         * 解锁LUA脚本
         */
        private static final RedisScript<Boolean> LUA_UNLOCK =
                RedisScript.of(new ClassPathResource("META-INF/script/spring.turbo.module.redis.lock.ReentrantLock#unlock.lua"), Boolean.class);
    }

}
