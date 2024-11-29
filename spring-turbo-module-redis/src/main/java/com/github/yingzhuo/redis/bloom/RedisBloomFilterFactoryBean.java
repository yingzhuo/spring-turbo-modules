package com.github.yingzhuo.redis.bloom;

import com.github.yingzhuo.redis.util.RedisUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import spring.turbo.util.hash.BloomFilter;

/**
 * BloomFilter工厂
 *
 * @author 应卓
 * @see RedisUtils#createDefaultBloomFilter(RedisOperations, String)
 * @since 3.4.0
 */
public class RedisBloomFilterFactoryBean implements FactoryBean<BloomFilter> {

    private final StringRedisTemplate stringRedisTemplate;
    private final String redisKey;

    /**
     * 构造方法
     *
     * @param stringRedisTemplate {@link StringRedisTemplate} 实例
     * @param redisKey            redis键
     */
    public RedisBloomFilterFactoryBean(StringRedisTemplate stringRedisTemplate, String redisKey) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisKey = redisKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BloomFilter getObject() {
        return RedisUtils.createDefaultBloomFilter(this.stringRedisTemplate, this.redisKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return RedisBloomFilter.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
