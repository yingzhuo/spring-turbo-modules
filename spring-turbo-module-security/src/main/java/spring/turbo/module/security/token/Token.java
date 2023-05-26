/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.token;

import spring.turbo.module.security.authentication.MutableAuthentication;
import spring.turbo.util.Asserts;

/**
 * 令牌
 *
 * @author 应卓
 *
 * @see #ofString(String)
 * @see spring.turbo.module.security.authentication.MutableAuthentication
 *
 * @since 1.0.0
 */
public interface Token extends MutableAuthentication {

    public static Token ofString(String string) {
        Asserts.notNull(string, "string is null");
        return StringToken.of(string);
    }

    public String asString();

    @Override
    public String toString();

}
