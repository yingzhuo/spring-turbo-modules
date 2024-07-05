package spring.turbo.module.security.jackson;

import com.fasterxml.jackson.annotation.*;
import spring.turbo.module.security.token.BasicToken;

/**
 * {@link BasicToken} Jackson Mixin
 *
 * @author 应卓
 * @see BasicToken
 * @since 1.2.3
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"stringValue", "username", "password"})
public abstract class BasicTokenMixin {

    // 反序列化
    @JsonCreator
    public static BasicToken toBasicToken(@JsonProperty("stringValue") String stringValue,
                                          @JsonProperty("username") String username, @JsonProperty("password") String password) {
        return new BasicToken(stringValue, username, password);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @JsonProperty("stringValue")
    public abstract String asString();

    @JsonGetter("username")
    public abstract String getUsername();

    @JsonGetter("password")
    public abstract String getPassword();

}
