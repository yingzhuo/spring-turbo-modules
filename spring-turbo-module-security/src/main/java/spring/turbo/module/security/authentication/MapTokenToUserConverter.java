package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.token.StringToken;
import spring.turbo.module.security.token.Token;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static spring.turbo.util.collection.CollectionUtils.nullSafeAddAll;

/**
 * @author 应卓
 * @since 2.2.2
 */
public class MapTokenToUserConverter implements TokenToUserConverter {

    private final Map<String, UserDetails> tokenToUserMap = new HashMap<>();

    /**
     * 默认构造方法
     */
    public MapTokenToUserConverter() {
    }

    public MapTokenToUserConverter(String rawToken, UserDetails userDetails) {
        this(Map.of(rawToken, userDetails));
    }

    public MapTokenToUserConverter(Map<String, UserDetails> tokenToUserMap) {
        nullSafeAddAll(this.tokenToUserMap, tokenToUserMap);
    }

    public MapTokenToUserConverter add(String rawToken, UserDetails userDetails) {
        this.tokenToUserMap.put(rawToken, userDetails);
        return this;
    }

    @Nullable
    @Override
    public UserDetails convert(@Nullable Token token) throws AuthenticationException {
        if (tokenToUserMap.isEmpty()) {
            return null;
        }

        if (token instanceof StringToken stringToken) {
            return this.tokenToUserMap.get(stringToken.asString());
        }
        return null;
    }

    public Map<String, UserDetails> getTokenToUserMap() {
        return Collections.unmodifiableMap(tokenToUserMap);
    }

}
