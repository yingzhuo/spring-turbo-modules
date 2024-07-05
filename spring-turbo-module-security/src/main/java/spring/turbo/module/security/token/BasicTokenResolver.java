package spring.turbo.module.security.token;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;
import spring.turbo.util.Base64Utils;
import spring.turbo.util.StringPool;

import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * HTTP Basic 令牌解析器
 *
 * @author 应卓
 * @see HeaderTokenResolver
 * @see BearerTokenResolver
 * @see HttpHeaders#AUTHORIZATION
 * @since 1.0.5
 */
public final class BasicTokenResolver extends HeaderTokenResolver {

    private static final String PREFIX = "Basic ";

    /**
     * 构造方法
     */
    public BasicTokenResolver() {
        super(HttpHeaders.AUTHORIZATION, PREFIX);
    }

    @Override
    public Optional<Token> resolve(WebRequest request) {
        final Optional<Token> tokenOption = super.resolve(request);

        if (tokenOption.isEmpty()) {
            return tokenOption;
        }

        final String tokenValue = tokenOption.get().asString();
        String headerValue = tokenValue;
        headerValue = new String(Base64Utils.decode(headerValue.getBytes(UTF_8)), UTF_8);

        final String[] parts = headerValue.split(StringPool.COLON);
        if (parts.length != 2) {
            return Optional.empty();
        } else {
            return Optional.of(new BasicToken(tokenValue, parts[0], parts[1]));
        }
    }

}
