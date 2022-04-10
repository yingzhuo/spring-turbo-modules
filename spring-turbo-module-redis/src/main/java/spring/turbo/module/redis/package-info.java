/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
@NonNullApi
@NonNullFields
package spring.turbo.module.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;

/**
 * @author 应卓
 * @since 1.0.15
 */
class SpringBootAutoConfiguration {

    @Bean(name = "redisLockLockLuaScript")
    RedisScript<Boolean> redisLockLockLuaScript() {
        final DefaultRedisScript<Boolean> bean = new DefaultRedisScript<>();
        bean.setLocation(new ClassPathResource("spring/turbo/module/redis/lua/lock-lock.lua"));
        bean.setResultType(Boolean.class);
        return bean;
    }

    @Bean(name = "redisLockReleaseLuaScript")
    RedisScript<Boolean> releaseScript() {
        final DefaultRedisScript<Boolean> bean = new DefaultRedisScript<>();
        bean.setLocation(new ClassPathResource("spring/turbo/module/redis/lua/lock-release.lua"));
        bean.setResultType(Boolean.class);
        return bean;
    }

}