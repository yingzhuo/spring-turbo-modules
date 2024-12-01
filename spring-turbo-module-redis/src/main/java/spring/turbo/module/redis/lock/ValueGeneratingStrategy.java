package spring.turbo.module.redis.lock;

import spring.turbo.util.StringPool;
import spring.turbo.util.UUIDUtils;
import spring.turbo.util.concurrent.CurrentThreadUtils;

import java.util.function.Function;

/**
 * LockValue 生成策略
 *
 * @author 应卓
 * @since 3.4.0
 */
public interface ValueGeneratingStrategy extends Function<String, String> {

    /**
     * 获取默认实现实例
     *
     * @return 默认实现
     */
    public static ValueGeneratingStrategy defaultInstance() {
        return __ -> UUIDUtils.uuid32() + StringPool.COLON + CurrentThreadUtils.getId();
    }

    /**
     * 获取UUID简易实现策略器
     *
     * @return UUID简易实现策略器
     * @see java.util.UUID
     */
    public static ValueGeneratingStrategy uuid() {
        return __ -> UUIDUtils.uuid32();
    }

    /**
     * 生成lock-value
     *
     * @param lockKey lock-key
     * @return lock-value
     */
    @Override
    public String apply(String lockKey);

}
