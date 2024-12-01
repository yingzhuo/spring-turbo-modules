package spring.turbo.module.redis.lock;

import org.springframework.core.style.ToStringCreator;
import spring.turbo.util.time.LocalDateTimeUtils;

import java.io.Serializable;

/**
 * 锁的戳记
 *
 * @author 应卓
 * @since 3.4.0
 */
public final class LockStamp implements Serializable {

    private final long timestamp = System.currentTimeMillis();
    private final String lockKey;
    private final String lockValue;
    private final boolean success;
    private final long ttl;

    /**
     * 构造方法
     *
     * @param lockKey   锁的键
     * @param lockValue 锁的值
     * @param ttl       过期时的时间戳
     * @param success   成功标志位
     */
    public LockStamp(String lockKey, String lockValue, long ttl, boolean success) {
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.ttl = ttl;
        this.success = success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("success", success)
                .append("key", lockKey)
                .append("value", lockValue)
                .append("ttl", LocalDateTimeUtils.toLocalDateTime(ttl, null))
                .append("timestamp", LocalDateTimeUtils.toLocalDateTime(timestamp, null))
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var lockStamp = (LockStamp) o;
        if (timestamp != lockStamp.timestamp) return false;
        if (success != lockStamp.success) return false;
        if (ttl != lockStamp.ttl) return false;
        if (!lockKey.equals(lockStamp.lockKey)) return false;
        return lockValue.equals(lockStamp.lockValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + lockKey.hashCode();
        result = 31 * result + lockValue.hashCode();
        result = 31 * result + (success ? 1 : 0);
        result = 31 * result + (int) (ttl ^ (ttl >>> 32));
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getLockValue() {
        return lockValue;
    }

    public long getTtl() {
        return ttl;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isNotSuccess() {
        return !success;
    }

}
