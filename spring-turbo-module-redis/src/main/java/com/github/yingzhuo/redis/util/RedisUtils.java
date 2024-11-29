package com.github.yingzhuo.redis.util;

import com.github.yingzhuo.redis.bloom.RedisBloomFilter;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import spring.turbo.util.hash.DigestHashFunctions;
import spring.turbo.util.hash.HashFunction;

/**
 * Redis相关工具
 *
 * @author 应卓
 * @since 3.4.0
 */
public final class RedisUtils {

    // 参考资料1: https://cloud.tencent.com/developer/article/2410509
    // 参考资料2: B站尚硅谷周阳老师视频资料

    private static final int DEFAULT_DELETE_ELEMENT_COUNT_PER_STEP = 100;

    /**
     * 私有构造方法
     */
    private RedisUtils() {
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 创建布隆过滤器
     *
     * @param redisOperations   RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key               Redis键
     * @param length            布隆过滤器长度
     * @param firstHashFunction 哈希函数
     * @param moreHashFunctions 其他哈希函数
     * @return 布隆过滤器实例
     */
    public static RedisBloomFilter createBloomFilter(
            RedisOperations<String, String> redisOperations,
            String key,
            int length,
            HashFunction firstHashFunction,
            HashFunction... moreHashFunctions) {
        return new RedisBloomFilter(redisOperations, key, length)
                .addHashFunctions(firstHashFunction, moreHashFunctions);
    }

    /**
     * 创建默认配置的布隆过滤器 <br>
     * <ul>
     *     <li>长度: 10_0000_0000</li>
     *     <li>哈希函数1: MD5</li>
     *     <li>哈希函数2: SHA-1</li>
     *     <li>哈希函数3: SHA-256</li>
     *     <li>哈希函数4: SHA-384</li>
     *     <li>哈希函数5: SHA-512</li>
     * </ul>
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key             Redis键
     * @return 布隆过滤器实例
     * @see HashFunction
     * @see DigestHashFunctions
     */
    public static RedisBloomFilter createDefaultBloomFilter(
            RedisOperations<String, String> redisOperations,
            String key) {
        return new RedisBloomFilter(redisOperations, key, 10_0000_0000)
                .addHashFunctions(
                        DigestHashFunctions.md5(),
                        DigestHashFunctions.sha1(),
                        DigestHashFunctions.sha256(),
                        DigestHashFunctions.sha384(),
                        DigestHashFunctions.sha512()
                );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 按照匹配模式删除 string 类型的数据
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param pattern         要匹配的模式
     * @param <V>             Value的泛型
     */
    public static <V> void deleteValuesByPattern(RedisOperations<String, V> redisOperations, String pattern) {
        deleteValuesByPattern(redisOperations, pattern, DEFAULT_DELETE_ELEMENT_COUNT_PER_STEP);
    }

    /**
     * 按照匹配模式删除 string 类型的数据
     *
     * @param redisOperations           RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param pattern                   要匹配的模式
     * @param deleteElementCountPerStep 每次删除的元素个数
     * @param <V>                       Value的泛型
     */
    public static <V> void deleteValuesByPattern(RedisOperations<String, V> redisOperations, String pattern, int deleteElementCountPerStep) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.hasText(pattern, "pattern is null or blank");
        Assert.isTrue(deleteElementCountPerStep >= 10, "deleteElementCountPerStep should >= 10");

        // @formatter:off
        var scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .type(DataType.STRING)
                .build();
        // @formatter:on

        try (var c = redisOperations.scan(scanOptions)) {
            while (c.hasNext()) {
                var key = c.next();
                redisOperations.delete(key);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 删除HASH类型BigKey
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key             要删除的键
     * @param <V>             Value的泛型
     */
    public static <V> void deleteBigHash(RedisOperations<String, V> redisOperations, String key) {
        deleteBigHash(redisOperations, key, DEFAULT_DELETE_ELEMENT_COUNT_PER_STEP);
    }

    /**
     * 删除HASH类型BigKey
     *
     * @param redisOperations           RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key                       要删除的键
     * @param deleteElementCountPerStep 每次删除的元素个数
     * @param <V>                       Value的泛型
     */
    public static <V> void deleteBigHash(RedisOperations<String, V> redisOperations, String key, int deleteElementCountPerStep) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.hasText(key, "key is null or blank");
        Assert.isTrue(deleteElementCountPerStep >= 10, "cursorCount should >= 10");

        var hashOp = redisOperations.opsForHash();

        // @formatter:off
        var scanOptions = ScanOptions.scanOptions()
                .count(deleteElementCountPerStep)
                .match("*")
                .build();
        // @formatter:on

        try (var c = hashOp.scan(key, scanOptions)) {
            while (c.hasNext()) {
                var entryKey = c.next().getKey();
                hashOp.delete(key, entryKey);
            }
        }

        redisOperations.delete(key);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 删除LIST类型的BigKey
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key             要删除的键
     * @param <V>             Value的泛型
     */
    public static <V> void deleteBigList(RedisOperations<String, V> redisOperations, String key) {
        deleteBigList(redisOperations, key, DEFAULT_DELETE_ELEMENT_COUNT_PER_STEP);
    }

    /**
     * 删除LIST类型的BigKey
     *
     * @param redisOperations           RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key                       要删除的键
     * @param deleteElementCountPerStep 每次删除的元素个数
     * @param <V>                       Value的泛型
     */
    public static <V> void deleteBigList(RedisOperations<String, V> redisOperations, String key, int deleteElementCountPerStep) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.notNull(key, "key is null or blank");
        Assert.isTrue(deleteElementCountPerStep >= 10, "deleteElementCountPerStep should >= 10");

        var listOp = redisOperations.opsForList();
        var size = listOp.size(key);
        var count = 0L;

        if (size == null || size.equals(0L)) {
            return;
        }

        while (count < size) {
            listOp.trim(key, 0, deleteElementCountPerStep);
            count += deleteElementCountPerStep;
        }

        redisOperations.delete(key);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 删除SET类型的BigKey
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key             要删除的键
     * @param <V>             Value的泛型
     */
    public static <V> void deleteBigSet(RedisOperations<String, V> redisOperations, String key) {
        deleteBigSet(redisOperations, key, DEFAULT_DELETE_ELEMENT_COUNT_PER_STEP);
    }

    /**
     * 删除SET类型的BigKey
     *
     * @param redisOperations           RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key                       要删除的键
     * @param deleteElementCountPerStep 每次删除的元素个数
     * @param <V>                       Value的泛型
     */
    public static <V> void deleteBigSet(RedisOperations<String, V> redisOperations, String key, int deleteElementCountPerStep) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.hasText(key, "key is null or blank");
        Assert.isTrue(deleteElementCountPerStep >= 10, "cursorCount should >= 10");

        var setOp = redisOperations.opsForSet();

        // @formatter:off
        var scanOptions = ScanOptions.scanOptions()
                .count(deleteElementCountPerStep)
                .match("*")
                .build();
        // @formatter:on

        try (var c = setOp.scan(key, scanOptions)) {
            while (c.hasNext()) {
                var item = c.next();
                setOp.remove(key, item);
            }
        }

        redisOperations.delete(key);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 删除ZSET类型的BigKey
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key             要删除的键
     * @param <V>             Value的泛型
     */
    public static <V> void deleteBigZset(RedisOperations<String, V> redisOperations, String key) {
        deleteBigSet(redisOperations, key, DEFAULT_DELETE_ELEMENT_COUNT_PER_STEP);
    }

    /**
     * 删除ZSET类型的BigKey
     *
     * @param redisOperations           RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param key                       要删除的键
     * @param deleteElementCountPerStep 每次删除的元素个数
     * @param <V>                       Value的泛型
     */
    public static <V> void deleteBigZset(RedisOperations<String, V> redisOperations, String key, int deleteElementCountPerStep) {
        Assert.notNull(redisOperations, "redisOperations is null");
        Assert.hasText(key, "key is null or blank");
        Assert.isTrue(deleteElementCountPerStep >= 10, "cursorCount should >= 10");

        var zsetOp = redisOperations.opsForZSet();

        // @formatter:off
        var scanOptions = ScanOptions.scanOptions()
                .count(deleteElementCountPerStep)
                .match("*")
                .build();
        // @formatter:on

        try (var c = zsetOp.scan(key, scanOptions)) {
            while (c.hasNext()) {
                var item = c.next();
                zsetOp.remove(key, item.getValue());
            }
        }

        redisOperations.delete(key);
    }

}
