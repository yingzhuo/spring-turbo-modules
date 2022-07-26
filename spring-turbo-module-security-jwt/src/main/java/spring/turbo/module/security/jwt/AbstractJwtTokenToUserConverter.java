/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.token.Token;

import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
public abstract class AbstractJwtTokenToUserConverter implements TokenToUserConverter {

    private final Algorithm algorithm;

    public AbstractJwtTokenToUserConverter(AlgorithmFactory algorithmFactory) {
        Asserts.notNull(algorithmFactory);
        Algorithm alg = algorithmFactory.create();
        Asserts.notNull(alg);
        this.algorithm = alg;
    }

    public AbstractJwtTokenToUserConverter(Algorithm algorithm) {
        Asserts.notNull(algorithm);
        this.algorithm = algorithm;
    }

    @Nullable
    @Override
    public final UserDetails convert(@Nullable Token token) throws AuthenticationException {

        if (token == null) {
            return null;
        }

        try {
            final String rawToken = token.asString();
            final Verification verification = JWT.require(algorithm);
            final JWTVerifier verifier = verification.build();
            DecodedJWT jwt = verifier.verify(rawToken);
            return doAuthenticate(rawToken, jwt).orElse(null);
        } catch (JWTVerificationException e) {
            throw VerificationExceptionTransformer.transform(e);
        }
    }

    protected abstract Optional<UserDetails> doAuthenticate(@NonNull String rawToken, DecodedJWT jwt) throws AuthenticationException;

}
