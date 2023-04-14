/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.token.StringToken;
import spring.turbo.module.security.token.Token;

import java.util.HashMap;
import java.util.Map;

import static spring.turbo.util.CollectionUtils.nullSafeAddAll;

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
        super();
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
    public UserDetails convert(Token token) throws AuthenticationException {
        if (token instanceof StringToken stringToken) {
            return this.tokenToUserMap.get(stringToken.asString());
        }
        return null;
    }

}
