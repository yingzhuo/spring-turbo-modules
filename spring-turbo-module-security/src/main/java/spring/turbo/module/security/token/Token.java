/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.token;

import spring.turbo.util.Asserts;

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
        Asserts.notNull(stringValue, "stringValue is null");
        return StringToken.of(stringValue);
    }

    public String asString();

    default int length() {
        return asString().length();
    }

}
