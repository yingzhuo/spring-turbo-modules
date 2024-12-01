package spring.turbo.module.redis.lock;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
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
 * @since 3.4.0
 */
public final class Lock implements Serializable {

    private final RedisOperations<String, String> redisOperations;
    private final ValueGeneratingStrategy valueGeneratingStrategy;

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
        var success = redisOperations.opsForValue()
                .setIfAbsent(lockKey, lockValue, lockTimeToLive);

        // 没有必要用lua脚本
//        var lua = "return redis.call('SET', ARGV[1], ARGV[2], 'NX', 'PX', ARGV[3])";
//        var ret = redisOperations.execute(
//                RedisScript.of(lua, String.class),
//                List.of(),
//                lockKey,
//                lockValue,
//                String.valueOf(lockTimeToLive.toMillis())
//        );
        return new LockStamp(lockKey, lockValue, System.currentTimeMillis() + lockTimeToLive.toMillis(), success);
    }

    /**
     * 解锁
     *
     * @param lockStamp 加锁戳记
     * @return 返回true时表示成功解锁
     */
    public boolean unlock(@Nullable LockStamp lockStamp) {
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

        var ret = redisOperations.execute(
                RedisScript.of(lua, String.class),
                List.of(),
                lockStamp.getLockKey(),
                lockStamp.getLockValue()
        );

        return StringPool.OK.equalsIgnoreCase(ret);
    }

}
