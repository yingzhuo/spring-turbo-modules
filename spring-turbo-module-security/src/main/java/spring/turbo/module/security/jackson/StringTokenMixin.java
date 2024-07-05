package spring.turbo.module.security.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import spring.turbo.module.security.token.StringToken;

/**
 * {@link StringToken} Jackson Mixin
 *
 * @author 应卓
 * @see StringToken
 * @since 2.0.3
 */
public abstract class StringTokenMixin {

    // 反序列化
    @JsonCreator
    public static StringToken toToken(String tokenValue) {
        return StringToken.of(tokenValue);
    }

    @JsonValue
    public abstract String asString();

}
