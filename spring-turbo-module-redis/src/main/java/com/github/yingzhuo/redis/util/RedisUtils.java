package com.github.yingzhuo.redis.util;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

/**
 * Redis相关工具
 *
 * @author 应卓
 * @since 3.4.0
 */
public final class RedisUtils {

    private static final int DEFAULT_CURSOR_COUNT = 15;

    /**
     * 私有构造方法
     */
    private RedisUtils() {
    }

    /**
     * 按照匹配模式删除 string 类型的数据
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param pattern         要匹配的模式
     * @param <K>             Key的泛型
     * @param <V>             Value的泛型
     * @return 删除的数据数
     */
    public static <K, V> long deleteValuesByPattern(RedisOperations<K, V> redisOperations, String pattern) {
        return deleteValuesByPattern(redisOperations, pattern, DEFAULT_CURSOR_COUNT);
    }

    /**
     * 按照匹配模式删除 string 类型的数据
     *
     * @param redisOperations RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param pattern         要匹配的模式
     * @param cursorCount     游标每一页的数据数
     * @param <K>             Key的泛型
     * @param <V>             Value的泛型
     * @return 删除的数据数
     */
    public static <K, V> long deleteValuesByPattern(RedisOperations<K, V> redisOperations, String pattern, int cursorCount) {
        Assert.notNull(redisOperations, "redisTemplate is null");
        Assert.hasText(pattern, "pattern is null or blank");
        Assert.isTrue(cursorCount >= 10, "cursorCount should >= 10");

        // @formatter:off
        var scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .type(DataType.STRING)
                .build();
        // @formatter:on

        long result = 0L;
        try (var c = redisOperations.scan(scanOptions)) {
            while (c.hasNext()) {
                var key = c.next();
                redisOperations.delete(key);
                result++;
            }
        }
        return result;
    }

}
