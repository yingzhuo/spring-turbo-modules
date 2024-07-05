package spring.turbo.module.dataaccessing.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnBean(RedisTemplate.class)
public class RedisLockUtilsAutoConfiguration {

    @Bean(name = "redisLockLockLuaScript")
    public RedisScript<Boolean> redisLockLockLuaScript() {
        final DefaultRedisScript<Boolean> bean = new DefaultRedisScript<>();
        bean.setLocation(new ClassPathResource("spring/turbo/module/dataaccessing/redis/lock-lock.lua"));
        bean.setResultType(Boolean.class);
        return bean;
    }

    @Bean(name = "redisLockReleaseLuaScript")
    public RedisScript<Boolean> releaseScript() {
        final DefaultRedisScript<Boolean> bean = new DefaultRedisScript<>();
        bean.setLocation(new ClassPathResource("spring/turbo/module/dataaccessing/redis/lock-release.lua"));
        bean.setResultType(Boolean.class);
        return bean;
    }

}
