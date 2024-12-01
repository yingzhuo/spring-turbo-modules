package spring.turbo.module.redis.lock;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * 基于Redis实现的分布式锁
 *
 * @author 应卓
 * @since 3.4.0
 */
public interface Lock extends Serializable {

    /**
     * 创建可重入锁
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @return 可重入锁
     */
    public static Lock reentrantLock(RedisOperations<String, String> redisOperations) {
        return new ReentrantLock(redisOperations, null);
    }

    /**
     * 创建可重入锁
     *
     * @param redisOperations         RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param valueGeneratingStrategy value生成策略
     * @return 可重入锁
     */
    public static Lock reentrantLock(RedisOperations<String, String> redisOperations, @Nullable ValueGeneratingStrategy valueGeneratingStrategy) {
        return new ReentrantLock(redisOperations, valueGeneratingStrategy);
    }

    /**
     * 加锁
     *
     * @param lockKey      键
     * @param ttlInSeconds ttl (秒)
     * @return 加锁戳记
     * @see LockStamp#isSuccess()
     */
    public LockStamp lock(String lockKey, long ttlInSeconds);

    /**
     * 解锁
     *
     * @return 返回true时表示成功解锁
     */
    public boolean unlock();

}
