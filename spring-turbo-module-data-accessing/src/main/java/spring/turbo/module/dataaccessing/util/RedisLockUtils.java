package spring.turbo.module.dataaccessing.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import spring.turbo.core.SpringUtils;
import spring.turbo.module.dataaccessing.redis.RedisLockKeyCustomizer;

import java.util.Collections;
import java.util.List;

/**
 * 分布式锁工具
 *
 * @author 应卓
 * @since 1.0.15
 */
@SuppressWarnings("unchecked")
public final class RedisLockUtils {

    private static final List<String> EMPTY_KEYS = Collections.emptyList();
    private static final RedisLockKeyCustomizer LOCK_KEY_CUSTOMIZER = RedisLockKeyCustomizer.DEFAULT;

    /**
     * 私有构造方法
     */
    private RedisLockUtils() {
    }

    /**
     * 锁
     *
     * @param key          锁定键
     * @param uuid         UUID
     * @param ttlInSeconds 自动解锁时间 (秒)
     * @return true时表示成功
     */
    public static boolean lock(String key, String uuid, int ttlInSeconds) {
        final RedisLockKeyCustomizer keyFunc = SpringUtils.getBean(RedisLockKeyCustomizer.class)
                .orElse(LOCK_KEY_CUSTOMIZER);
        key = keyFunc.customize(key);

        final StringRedisTemplate redisTemplate = SpringUtils.getRequiredBean(StringRedisTemplate.class);
        final RedisScript<Boolean> lua = SpringUtils.getRequiredBean(RedisScript.class, "redisLockLockLuaScript");
        final Boolean result = redisTemplate.execute(lua, EMPTY_KEYS, key, uuid, String.valueOf(ttlInSeconds));
        return result != null ? result : false;
    }

    /**
     * 解锁
     *
     * @param key  锁定键
     * @param uuid UUID
     * @return true时表示成功
     */
    public static boolean release(String key, String uuid) {
        final RedisLockKeyCustomizer keyFunc = SpringUtils.getBean(RedisLockKeyCustomizer.class)
                .orElse(LOCK_KEY_CUSTOMIZER);
        key = keyFunc.customize(key);

        final StringRedisTemplate redisTemplate = SpringUtils.getRequiredBean(StringRedisTemplate.class);
        final RedisScript<Boolean> lua = SpringUtils.getRequiredBean(RedisScript.class, "redisLockReleaseLuaScript");
        final Boolean result = redisTemplate.execute(lua, EMPTY_KEYS, key, uuid);
        return result != null ? result : false;
    }

}
