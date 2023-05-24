/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import spring.turbo.module.security.util.AuthorityUtils;
import spring.turbo.util.Asserts;

import java.util.Objects;

/**
 * String令牌
 *
 * @author 应卓
 *
 * @since 1.0.0
 */
public final class StringToken extends AbstractAuthenticationToken implements Token {

    private final String string;

    public StringToken(CharSequence string) {
        super(AuthorityUtils.noAuthorities());
        Asserts.notNull(string);
        this.string = string.toString();
        this.setAuthenticated(false);
        this.setDetails(null);
    }

    public static StringToken of(CharSequence token) {
        return new StringToken(token);
    }

    @Override
    public Object getCredentials() {
        return string;
    }

    @Override
    public Object getPrincipal() {
        return string;
    }

    @Override
    public String asString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StringToken that = (StringToken) o;
        return string.equals(that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

}
