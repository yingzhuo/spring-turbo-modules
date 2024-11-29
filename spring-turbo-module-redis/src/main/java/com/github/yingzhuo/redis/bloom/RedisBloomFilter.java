package com.github.yingzhuo.redis.bloom;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;
import spring.turbo.util.hash.BloomFilter;
import spring.turbo.util.hash.HashFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static spring.turbo.util.collection.CollectionUtils.nullSafeAdd;
import static spring.turbo.util.collection.CollectionUtils.nullSafeAddAll;

/**
 * 基于BITMAP实现的布隆过滤器
 *
 * @author 应卓
 * @since 3.4.0
 */
public class RedisBloomFilter implements BloomFilter {

    private static final int DEFAULT_LENGTH = 10_0000_0000; // 十亿

    private final List<HashFunction> hashFunctions = new ArrayList<>(10);
    private final RedisOperations<String, String> redisOperations;
    private final String redisKey;
    private final int length;

    public RedisBloomFilter(RedisOperations<String, String> redisOperations, String redisKey) {
        this(redisOperations, redisKey, DEFAULT_LENGTH);
    }

    public RedisBloomFilter(RedisOperations<String, String> redisOperations, String redisKey, int length) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.hasText(redisKey, "redisKey is null or empty");
        Assert.isTrue(length >= 1000_0000, "length should >= 10000000");

        this.redisOperations = redisOperations;
        this.redisKey = redisKey;
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }

    public List<HashFunction> getHashFunctions() {
        return Collections.unmodifiableList(hashFunctions);
    }

    public RedisBloomFilter addHashFunctions(HashFunction first, HashFunction... moreFunctions) {
        nullSafeAdd(hashFunctions, first);
        nullSafeAddAll(hashFunctions, moreFunctions);
        return this;
    }

    @Override
    public void add(String element) {
        Assert.notNull(element, "element is null");
        Assert.notEmpty(hashFunctions, "hashFunctions is empty");

        hashFunctions.forEach(func -> {
            var offset = func.apply(element) % length;
            offset = Math.abs(offset);
            redisOperations.opsForValue()
                    .setBit(this.redisKey, offset, true);
        });
    }

    @Override
    public boolean exists(String element) {
        Assert.notNull(element, "element is null");
        Assert.notEmpty(hashFunctions, "hashFunctions is empty");

        for (HashFunction func : hashFunctions) {
            var offset = func.apply(element) % length;
            offset = Math.abs(offset);
            var b = redisOperations.opsForValue()
                    .getBit(redisKey, offset);

            if (!b) {
                return false;
            }
        }

        return true;
    }

}
