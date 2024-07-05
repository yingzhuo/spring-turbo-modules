package spring.turbo.module.jwt.factory.delegate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import spring.turbo.module.jwt.factory.JsonWebTokenData;
import spring.turbo.module.jwt.factory.JsonWebTokenFactory;

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
public class JavaJwtJsonWebTokenFactory implements JsonWebTokenFactory {

    /**
     * 签名算法
     */
    private final Algorithm algorithm;

    /**
     * 构造方法
     *
     * @param algorithm 签名算法
     */
    private JavaJwtJsonWebTokenFactory(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 创建 {@link JavaJwtJsonWebTokenFactory} 实例
     *
     * @param algorithm 签名算法
     * @return {@link JavaJwtJsonWebTokenFactory} 实例
     */
    public static JavaJwtJsonWebTokenFactory of(Algorithm algorithm) {
        return new JavaJwtJsonWebTokenFactory(algorithm);
    }

    public static JavaJwtJsonWebTokenFactory none() {
        return of(Algorithm.none());
    }

    public static JavaJwtJsonWebTokenFactory HMAC256(String secret) {
        return of(Algorithm.HMAC256(secret));
    }

    public static JavaJwtJsonWebTokenFactory HMAC384(String secret) {
        return of(Algorithm.HMAC384(secret));
    }

    public static JavaJwtJsonWebTokenFactory HMAC512(String secret) {
        return of(Algorithm.HMAC512(secret));
    }

    public static JavaJwtJsonWebTokenFactory ECDSA256(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.ECDSA256((ECPublicKey) publicKey, (ECPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenFactory ECDSA384(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.ECDSA384((ECPublicKey) publicKey, (ECPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenFactory ECDSA512(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.ECDSA512((ECPublicKey) publicKey, (ECPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenFactory RSA256(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenFactory RSA384(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.RSA384((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    public static JavaJwtJsonWebTokenFactory RSA512(PublicKey publicKey, PrivateKey privateKey) {
        return of(Algorithm.RSA512((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey));
    }

    @Override
    public String apply(JsonWebTokenData data) {
        return JWT.create()
                .withHeader(data.getHeaderMap())
                .withPayload(data.getPayloadMap())
                .sign(algorithm);
    }

}
