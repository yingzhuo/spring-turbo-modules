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
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import spring.turbo.util.Asserts;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class JwtTokenFactoryImpl implements JwtTokenFactory {

    private final Algorithm algorithm;

    public JwtTokenFactoryImpl(AlgorithmFactory algorithmFactory) {
        Asserts.notNull(algorithmFactory);
        Algorithm alg = algorithmFactory.create();
        Asserts.notNull(alg);
        this.algorithm = alg;
    }

    @Override
    public String create(JwtTokenMetadata metadata) {

        final JWTCreator.Builder builder = JWT.create();

        Optional.ofNullable(metadata.getKeyId()).ifPresent(builder::withKeyId);
        Optional.ofNullable(metadata.getJwtId()).ifPresent(builder::withJWTId);
        Optional.ofNullable(metadata.getIssuer()).ifPresent(builder::withIssuer);
        Optional.ofNullable(metadata.getSubject()).ifPresent(builder::withSubject);
        Optional.ofNullable(metadata.getExpiresAt()).ifPresent(builder::withExpiresAt);
        Optional.ofNullable(metadata.getNotBefore()).ifPresent(builder::withNotBefore);
        Optional.ofNullable(metadata.getIssuedAt()).ifPresent(builder::withIssuedAt);
        Optional.ofNullable(metadata.getAudience()).ifPresent(it -> {
            if (!it.isEmpty()) {
                builder.withAudience(metadata.getAudience().toArray(new String[0]));
            }
        });

        // Private Claims
        Optional.ofNullable(metadata.getPayloadClaims()).ifPresent(map -> {
            final Set<String> keySet = map.keySet();
            for (String name : keySet) {
                Object value = map.get(name);

                if (value instanceof String) {
                    builder.withClaim(name, (String) value);
                    continue;
                }

                if (value instanceof Integer) {
                    builder.withClaim(name, (Integer) value);
                    continue;
                }

                if (value instanceof Boolean) {
                    builder.withClaim(name, (Boolean) value);
                    continue;
                }

                if (value instanceof Date) {
                    builder.withClaim(name, (Date) value);
                    continue;
                }

                if (value instanceof Long) {
                    builder.withClaim(name, (Long) value);
                    continue;
                }

                if (value instanceof Double) {
                    builder.withClaim(name, (Double) value);
                    continue;
                }

                if (value instanceof String[]) {
                    builder.withArrayClaim(name, (String[]) value);
                    continue;
                }

                if (value instanceof Integer[]) {
                    builder.withArrayClaim(name, (Integer[]) value);
                    continue;
                }

                if (value instanceof Long[]) {
                    builder.withArrayClaim(name, (Long[]) value);
                }
            }
        });

        return builder.sign(algorithm);
    }

}
