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
 * @author 应卓
 *
 * @see BasicTokenResolver
 *
 * @since 1.2.3
 */
public final class BasicToken extends AbstractAuthenticationToken implements Token {

    /**
     * 用户名
     */
    private final String username;

    /**
     * 口令
     */
    private final String password;

    /**
     * 令牌原字符串
     */
    private final String string;

    /**
     * 构造方法
     *
     * @param stringValue
     *            令牌原字符串
     * @param username
     *            用户名
     * @param password
     *            口令
     */
    public BasicToken(String stringValue, String username, String password) {
        super(AuthorityUtils.noAuthorities());
        Asserts.hasText(stringValue);
        Asserts.hasText(username);
        Asserts.hasText(password);
        this.username = username;
        this.password = password;
        this.string = stringValue;
        this.setAuthenticated(false);
        this.setDetails(null);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getPassword();
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
        BasicToken that = (BasicToken) o;
        return string.equals(that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

}
