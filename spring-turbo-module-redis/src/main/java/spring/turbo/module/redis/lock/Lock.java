package spring.turbo.module.redis.lock;

import java.time.Duration;

/**
 * 基于Redis实现的分布式锁
 *
 * @author 应卓
 * @since 3.4.0
 */
public interface Lock {

    /**
     * 加锁
     *
     * @param lockKey        键
     * @param lockTimeToLive ttl
     * @return 加锁戳记
     * @see LockStamp#isSuccess()
     */
    public LockStamp lock(String lockKey, Duration lockTimeToLive);

    /**
     * 解锁
     *
     * @return 返回true时表示成功解锁
     */
    public boolean unlock();

}
