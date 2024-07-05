package spring.turbo.module.dataaccessing.redis;

/**
 * @author 应卓
 * @since 1.3.0
 */
@FunctionalInterface
public interface RedisLockKeyCustomizer {

    public static final RedisLockKeyCustomizer DEFAULT = key -> key;

    public String customize(String key);

}
