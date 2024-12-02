package spring.turbo.module.redis.lock;

import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;
import spring.turbo.util.time.LocalDateTimeUtils;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Timer;

/**
 * 锁帧 <br>
 * 用于记录锁成功时的一些基本信息
 *
 * @author 应卓
 * @since 3.4.0
 */
public final class LockFrame implements Serializable {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * 锁成功时的时间戳
     */
    private final long creationTimestamp;
    private final String lockKey;
    private final String lockField;
    private final long ttlInSeconds;
    private final long reentrantCount;
    private final long threadId;
    private final String threadName;

    public LockFrame(long creationTimestamp, String lockKey, String lockField, long ttlInSeconds, long reentrantCount, long threadId, String threadName) {
        this.creationTimestamp = creationTimestamp;
        this.lockKey = lockKey;
        this.lockField = lockField;
        this.ttlInSeconds = ttlInSeconds;
        this.reentrantCount = reentrantCount;
        this.threadId = threadId;
        this.threadName = threadName;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    private Timer timer = null;

    Optional<Timer> getNullableTimer() {
        return Optional.ofNullable(timer);
    }

    void setTimer(Timer timer) {
        this.timer = timer;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("creation", LocalDateTimeUtils.toLocalDateTime(creationTimestamp, null).format(DEFAULT_TIME_FORMATTER))
                .append("lock-key", lockKey)
                .append("lock-field", lockField)
                .append("ttl", ttlInSeconds)
                .append("reentrant-count", reentrantCount)
                .append("thread-id", threadId)
                .append("thread-name", threadName)
                .toString();
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getLockField() {
        return lockField;
    }

    public long getTtlInSeconds() {
        return ttlInSeconds;
    }

    public long getReentrantCount() {
        return reentrantCount;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getThreadName() {
        return threadName;
    }

}
