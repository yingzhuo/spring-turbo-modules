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
 *
 * @see #ofString(CharSequence)
 *
 * @since 1.0.0
 */
public interface Token extends Serializable {

    public static Token ofString(CharSequence string) {
        Asserts.notNull(string, "string is null");
        return StringToken.of(string.toString());
    }

    public String asString();

    @Override
    public String toString();

}
