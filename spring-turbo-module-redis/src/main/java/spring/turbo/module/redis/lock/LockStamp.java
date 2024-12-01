package spring.turbo.module.redis.lock;

import lombok.Getter;
import org.springframework.core.style.ToStringCreator;
import spring.turbo.util.time.LocalDateTimeUtils;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 * 锁的戳记
 *
 * @author 应卓
 * @since 3.4.0
 */
@Getter
public final class LockStamp implements Serializable {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private final long creationTimestamp = System.currentTimeMillis();
    private final boolean success;
    private final String lockKey;
    private final String lockValue;
    private final long unlockTimestamp;
    private final long ttlDurationInSeconds;

    /**
     * 构造方法
     *
     * @param success         成功标志位
     * @param lockKey         锁的键
     * @param lockValue       锁的值
     * @param unlockTimestamp 过期时的时间戳
     */
    public LockStamp(boolean success, String lockKey, String lockValue, long unlockTimestamp, long ttlDurationInSeconds) {
        this.success = success;
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.unlockTimestamp = unlockTimestamp;
        this.ttlDurationInSeconds = ttlDurationInSeconds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("success", success)
                .append("creation-time", LocalDateTimeUtils.toLocalDateTime(creationTimestamp, null).format(DEFAULT_TIME_FORMATTER))
                .append("unlock-time", LocalDateTimeUtils.toLocalDateTime(unlockTimestamp, null).format(DEFAULT_TIME_FORMATTER))
                .append("ttl-duration-in-seconds", ttlDurationInSeconds)
                .append("lock-key", lockKey)
                .append("lock-value", lockValue)
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
        if (creationTimestamp != lockStamp.creationTimestamp) return false;
        if (success != lockStamp.success) return false;
        if (unlockTimestamp != lockStamp.unlockTimestamp) return false;
        if (!lockKey.equals(lockStamp.lockKey)) return false;
        return lockValue.equals(lockStamp.lockValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = (int) (creationTimestamp ^ (creationTimestamp >>> 32));
        result = 31 * result + lockKey.hashCode();
        result = 31 * result + lockValue.hashCode();
        result = 31 * result + (success ? 1 : 0);
        result = 31 * result + (int) (unlockTimestamp ^ (unlockTimestamp >>> 32));
        return result;
    }

    public boolean isNotSuccess() {
        return !success;
    }

}
