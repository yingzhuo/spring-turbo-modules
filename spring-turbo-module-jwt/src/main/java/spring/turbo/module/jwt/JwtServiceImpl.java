package spring.turbo.module.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.lang.Nullable;
import spring.turbo.module.jwt.alg.JwtSigner;
import spring.turbo.module.jwt.alg.KeyPairJwtSigner;
import spring.turbo.module.jwt.alg.SecretKeyJwtSigner;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.Optional;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class JwtServiceImpl implements JwtService {

    private final JwtSigner signer;

    public JwtServiceImpl(JwtSigner signer) {
        this.signer = signer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createToken(JwtData data) {
        return Jwts.builder()
                .header()
                .add(data.getHeaderMap())
                .and()
                .claims(data.getPayloadMap())
                .signWith(getSignerKey())
                .compact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatingResult validateToken(String token) {
        var builder = Jwts.parser();

        Optional.ofNullable(getVerifyPublicKey())
                .ifPresent(builder::verifyWith);

        Optional.ofNullable(getVerifySecretKey())
                .ifPresent(builder::verifyWith);

        try {
            builder.build()
                    .parse(token);
        } catch (ExpiredJwtException | PrematureJwtException e) {
            return ValidatingResult.INVALID_TIME;
        } catch (SecurityException e) {
            return ValidatingResult.INVALID_SIGNATURE;
        } catch (MalformedJwtException | IllegalArgumentException e) {
            return ValidatingResult.INVALID_JWT_FORMAT;
        }

        return ValidatingResult.OK;
    }

    private Key getSignerKey() {
        if (signer instanceof KeyPairJwtSigner keyPairJwtSigner) {
            return keyPairJwtSigner.keyPair().getPrivate();
        }

        if (signer instanceof SecretKeyJwtSigner secretKeyJwtSigner) {
            return secretKeyJwtSigner.secretKey();
        }

        var msg = String.format("unsupported type: %s", signer.getClass().getName());
        throw new IllegalStateException(msg);
    }

    @Nullable
    private PublicKey getVerifyPublicKey() {
        if (signer instanceof KeyPairJwtSigner keyPairJwtSigner) {
            return keyPairJwtSigner.keyPair().getPublic();
        }
        return null;
    }

    @Nullable
    private SecretKey getVerifySecretKey() {
        if (signer instanceof SecretKeyJwtSigner secretKeyJwtSigner) {
            return secretKeyJwtSigner.secretKey();
        }
        return null;
    }

}
