/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.token;

import org.springframework.lang.NonNull;
import org.springframework.web.context.request.WebRequest;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.token.BearerTokenResolver;
import spring.turbo.webmvc.token.StringToken;
import spring.turbo.webmvc.token.Token;
import spring.turbo.webmvc.token.TokenResolver;

import java.util.Optional;

import static spring.turbo.util.StringPool.DOT;

/**
 * @author 应卓
 * @since 1.0.9
 */
public class JwtTokenResolver implements TokenResolver {

    private final TokenResolver resolver;

    public JwtTokenResolver() {
        this(new BearerTokenResolver());
    }

    public JwtTokenResolver(@NonNull TokenResolver resolver) {
        Asserts.notNull(resolver);
        this.resolver = resolver;
    }

    @Override
    public Optional<Token> resolve(WebRequest request) {
        final Optional<Token> option = resolver.resolve(request);

        if (!option.isPresent()) {
            return Optional.empty();
        }

        final Token token = option.get();

        if (!(token instanceof StringToken)) {
            return option;
        }

        String rawToken = token.asString();

        final String[] parts = rawToken.split("\\.");

        if (parts.length == 2 && !rawToken.endsWith(DOT)) {
            return Optional.of(StringToken.of(rawToken + DOT));
        }

        return option;
    }

}
