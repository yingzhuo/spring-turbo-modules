package spring.turbo.module.security.token;

import spring.turbo.util.Asserts;

import java.util.Objects;

/**
 * String令牌
 *
 * @author 应卓
 * @since 1.0.0
 */
public final class StringToken implements Token {

    private final String string;

    public StringToken(String tokenValue) {
        Asserts.hasText(tokenValue, "tokenValue is null or blank");
        this.string = tokenValue;
    }

    public static StringToken of(String token) {
        return new StringToken(token);
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
