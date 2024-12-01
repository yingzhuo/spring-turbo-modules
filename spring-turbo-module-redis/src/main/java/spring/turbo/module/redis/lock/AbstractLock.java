package spring.turbo.module.redis.lock;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * 分布式锁共用基类
 *
 * @author 应卓
 * @since 3.4.0
 */
public abstract class AbstractLock implements Lock, Serializable {

    @NonNull
    protected final RedisOperations<String, String> redisOperations;

    @NonNull
    protected final ValueGeneratingStrategy valueGeneratingStrategy;

    @Nullable
    protected LockStamp lastSuccessLockStamp = null;

    protected AbstractLock(RedisOperations<String, String> redisOperations, @Nullable ValueGeneratingStrategy valueGeneratingStrategy) {
        Assert.notNull(redisOperations, "redisOperations is required");
        this.redisOperations = redisOperations;
        this.valueGeneratingStrategy = Objects.requireNonNullElseGet(valueGeneratingStrategy, ValueGeneratingStrategy::defaultInstance);
    }

    /**
     * 获取最后一次成功时的戳记
     *
     * @return 戳记实例
     */
    @Nullable
    public final LockStamp getLastSuccessLockStamp() {
        return lastSuccessLockStamp;
    }

}
