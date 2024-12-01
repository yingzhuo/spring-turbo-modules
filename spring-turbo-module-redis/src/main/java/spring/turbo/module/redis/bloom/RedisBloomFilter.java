package spring.turbo.module.redis.bloom;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import spring.turbo.util.collection.CollectionUtils;
import spring.turbo.util.hash.BloomFilter;
import spring.turbo.util.hash.HashFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基于BITMAP实现的布隆过滤器
 *
 * @author 应卓
 * @since 3.4.0
 */
public class RedisBloomFilter implements BloomFilter {

    private static final int DEFAULT_BITMAP_SIZE = 10_0000_0000; // 十亿

    private final List<HashFunction> hashFunctions = new ArrayList<>(5);
    private final RedisOperations<String, String> redisOperations;
    private final String redisKey;
    private final int bitmapSize;

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param redisKey        redis的键
     */
    public RedisBloomFilter(RedisOperations<String, String> redisOperations, String redisKey) {
        this(redisOperations, redisKey, DEFAULT_BITMAP_SIZE);
    }

    /**
     * 构造方法
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param redisKey        redis的键
     * @param bitmapSize      底层bitmap长度
     */
    public RedisBloomFilter(RedisOperations<String, String> redisOperations, String redisKey, int bitmapSize) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.hasText(redisKey, "redisKey is null or empty");
        Assert.isTrue(bitmapSize >= 1000_0000, "bitmapSize should >= 10000000");

        this.redisOperations = redisOperations;
        this.redisKey = redisKey;
        this.bitmapSize = bitmapSize;
    }

    /**
     * 获取底层bitmap长度
     *
     * @return 底层bitmap长度
     */
    public int getBitmapSize() {
        return bitmapSize;
    }

    /**
     * 获取已注册的哈希函数器
     *
     * @return 已注册的哈希函数器
     */
    public List<HashFunction> getHashFunctions() {
        return Collections.unmodifiableList(hashFunctions);
    }

    /**
     * 添加一个或多个哈希函数器
     *
     * @param first         第一个哈希函数器
     * @param moreFunctions 多个其他哈希函数器
     * @return this
     */
    public RedisBloomFilter addHashFunctions(HashFunction first, HashFunction... moreFunctions) {
        CollectionUtils.nullSafeAdd(hashFunctions, first);
        CollectionUtils.nullSafeAddAll(hashFunctions, moreFunctions);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(String element) {
        Assert.notNull(element, "element is null");
        Assert.notEmpty(hashFunctions, "hashFunctions is empty");

        hashFunctions.forEach(func -> {
            var offset = func.apply(element) % bitmapSize;
            offset = Math.abs(offset);
            redisOperations.opsForValue()
                    .setBit(redisKey, offset, true);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mightContain(@Nullable String element) {
        // null认为不存在
        if (element == null) {
            return false;
        }

        Assert.notEmpty(hashFunctions, "hashFunctions is empty");

        for (var func : hashFunctions) {
            var offset = func.apply(element) % bitmapSize;
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
