/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.validator.delegate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import spring.turbo.module.jwt.validator.JsonWebTokenValidator;
import spring.turbo.module.jwt.validator.ValidatingResult;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * java-jwt 工具库装饰器实现
 *
 * @author 应卓
 * @see <a href="https://github.com/auth0/java-jwt">java-jwt官方文档</a>
 * @since 3.1.1
 */
public class JavaJwtJsonWebTokenValidator implements JsonWebTokenValidator {

    /**
     * 签名算法
     */
    private final Algorithm algorithm;

    /**
     * 私有构造方法
     *
     * @param algorithm 签名算法
     */
    private JavaJwtJsonWebTokenValidator(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public static JavaJwtJsonWebTokenValidator of(Algorithm algorithm) {
        return new JavaJwtJsonWebTokenValidator(algorithm);
    }

    public static JavaJwtJsonWebTokenValidator none() {
        return of(Algorithm.none());
    }

    public static JavaJwtJsonWebTokenValidator HMAC256(String secret) {
        return of(Algorithm.HMAC256(secret));
    }

    public static JavaJwtJsonWebTokenValidator HMAC384(String secret) {
        return of(Algorithm.HMAC384(secret));
    }

    public static JavaJwtJsonWebTokenValidator HMAC512(String secret) {
        return of(Algorithm.HMAC512(secret));
    }

    public static JavaJwtJsonWebTokenValidator ECDSA256(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.ECDSA256((ECPublicKey) publicKey, (ECPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenValidator ECDSA384(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.ECDSA384((ECPublicKey) publicKey, (ECPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenValidator ECDSA512(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.ECDSA512((ECPublicKey) publicKey, (ECPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenValidator RSA256(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenValidator RSA384(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.RSA384((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenValidator RSA512(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.RSA512((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    @Override
    public ValidatingResult validate(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

            verifier.verify(token);
            return ValidatingResult.NO_PROBLEM;
        } catch (JWTVerificationException e) {
//            e.printStackTrace();

            if (e instanceof AlgorithmMismatchException) {
                return ValidatingResult.INVALID_SIGNATURE;
            }

            if (e instanceof SignatureVerificationException) {
                return ValidatingResult.INVALID_SIGNATURE;
            }

            if (e instanceof IncorrectClaimException) {

                var msg = e.getMessage();
                if (msg.matches("^The Token can't be used before.*$")) {
                    return ValidatingResult.INVALID_TIME;
                }

                return ValidatingResult.INVALID_JWT_FORMAT;
            }

            if (e instanceof InvalidClaimException) {
                return ValidatingResult.INVALID_JWT_FORMAT;
            }

            if (e instanceof JWTDecodeException) {
                return ValidatingResult.INVALID_JWT_FORMAT;
            }

            if (e instanceof TokenExpiredException) {
                return ValidatingResult.INVALID_TIME;
            }

            throw e;
        }
    }

}
