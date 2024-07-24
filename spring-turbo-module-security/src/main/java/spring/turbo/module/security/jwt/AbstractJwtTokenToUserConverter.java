package spring.turbo.module.security.jwt;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import spring.turbo.module.jwt.JwtAssertions;
import spring.turbo.module.jwt.JwtService;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.module.security.jwt.exception.BadJwtAlgorithmTokenException;
import spring.turbo.module.security.jwt.exception.BadJwtClaimTokenException;
import spring.turbo.module.security.jwt.exception.BadJwtFormatTokenException;
import spring.turbo.module.security.jwt.exception.BadJwtTimeTokenException;
import spring.turbo.module.security.token.Token;
import spring.turbo.util.StringFormatter;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author 应卓
 * @since 2.2.4
 */
public abstract class AbstractJwtTokenToUserConverter implements TokenToUserConverter {

    private final JwtService jwtService;

    /**
     * 构造方法
     *
     * @param jwtService JWT验证器
     */
    protected AbstractJwtTokenToUserConverter(JwtService jwtService) {
        Assert.notNull(jwtService, "validator is required");
        this.jwtService = jwtService;
    }

    @Nullable
    @Override
    public final UserDetails convert(@Nullable Token token) throws AuthenticationException {

        if (token == null) {
            return null;
        }

        var rawToken = token.asString();

        var parts = rawToken.split("\\.");
        if (parts.length != 3) {
            throw new BadJwtFormatTokenException(StringFormatter.format("invalid toke: {}", rawToken));
        }

        var result = jwtService.validateToken(token.asString(), this.getJwtAssertions());

        switch (result) {
            case INVALID_JWT_FORMAT:
                throw new BadJwtFormatTokenException(StringFormatter.format("invalid toke: {}", rawToken));
            case INVALID_SIGNATURE:
                throw new BadJwtAlgorithmTokenException(StringFormatter.format("invalid signature: {}", rawToken));
            case INVALID_TIME:
                throw new BadJwtTimeTokenException(StringFormatter.format("invalid time: {}", rawToken));
            case INVALID_CLAIM:
                throw new BadJwtClaimTokenException(StringFormatter.format("invalid claim: {}", rawToken));
            case OK:
                break;
        }

        var decoder = Base64.getUrlDecoder();
        var headerJson = new String(decoder.decode(parts[0].getBytes(UTF_8)));
        var payloadJson = new String(decoder.decode(parts[1].getBytes(UTF_8)));

        return doAuthenticate(
                rawToken,
                headerJson,
                payloadJson
        );
    }

    @Nullable
    protected abstract JwtAssertions getJwtAssertions();

    @Nullable
    protected abstract UserDetails doAuthenticate(
            String rawToken,
            String headerJson,
            String payloadJson) throws AuthenticationException;

}
