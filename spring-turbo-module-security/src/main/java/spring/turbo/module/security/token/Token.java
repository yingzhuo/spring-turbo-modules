package spring.turbo.module.security.token;

import java.io.Serializable;

/**
 * 令牌
 *
 * @author 应卓
 * @see #ofString(String)
 * @see spring.turbo.module.security.authentication.MutableAuthentication
 * @since 1.0.0
 */
public interface Token extends Serializable {

    public static Token ofString(String stringValue) {
        return StringToken.of(stringValue);
    }

    public String asString();

    default int length() {
        return asString().length();
    }

}
