package spring.turbo.module.redis.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.TimerTask;

/**
 * 分布式锁刷新TTL任务
 *
 * @author 应卓
 * @see java.util.Timer
 * @see ReentrantLock
 * @since 3.4.0
 */
public final class RenewTask extends TimerTask {

    private static final RedisScript<Boolean> RENEW_TTL =
            RedisScript.of(new ClassPathResource("META-INF/Lock#renew-ttl.lua"), Boolean.class);

    private final RedisOperations<String, String> redisOperations;
    private final String key;
    private final String field;
    private final long ttl;

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key             key
     * @param field           Hash field
     * @param ttl             自动过期时间
     */
    public RenewTask(RedisOperations<String, String> redisOperations, String key, String field, long ttl) {
        this.redisOperations = redisOperations;
        this.key = key;
        this.field = field;
        this.ttl = ttl;
    }

    @Override
    public void run() {
        redisOperations.execute(
                RENEW_TTL,
                List.of(),
                key,
                field,
                String.valueOf(ttl)
        );
    }

}
