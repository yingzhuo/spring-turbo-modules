package spring.turbo.module.security.token.blacklist;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import spring.turbo.module.security.exception.BlacklistTokenException;
import spring.turbo.module.security.token.Token;
import spring.turbo.util.Asserts;
import spring.turbo.util.StringFormatter;
import spring.turbo.util.StringPool;

import java.time.Duration;

/**
 * @author 应卓
 * @since 2.0.5
 */
public class RedisTokenBlacklistManager implements TokenBlacklistManager {

    private final StringRedisTemplate redisTemplate;

    @Nullable
    private final Duration ttl;

    private String keyPrefix = StringPool.EMPTY;
    private String keySuffix = StringPool.EMPTY;

    public RedisTokenBlacklistManager(StringRedisTemplate redisTemplate, @Nullable Duration ttl) {
        Asserts.notNull(redisTemplate);
        this.redisTemplate = redisTemplate;
        this.ttl = ttl;
    }

    @Override
    public void save(Token token) {
        Asserts.notNull(token);
        final var key = getKey(token);
        final var value = getValue(token);
        if (ttl == null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, this.ttl);
        }
    }

    @Override
    public void verify(Token token) throws BlacklistTokenException {
        Asserts.notNull(token);
        final var key = getKey(token);
        if (redisTemplate.opsForValue().get(key) != null) {
            var msg = StringFormatter.format("token \"{}\" is blacklisted", token.asString());
            throw new BlacklistTokenException(msg);
        }
    }

    protected String getKey(Token token) {
        return keyPrefix + token.asString() + keySuffix;
    }

    protected String getValue(Token token) {
        return token.asString();
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public void setKeySuffix(String keySuffix) {
        this.keySuffix = keySuffix;
    }

}
