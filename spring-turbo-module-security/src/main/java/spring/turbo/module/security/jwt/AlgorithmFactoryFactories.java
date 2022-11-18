/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @see AlgorithmFactory
 * @see Algorithm
 * @see ECDSA256AlgorithmFactory
 * @see ECDSA384AlgorithmFactory
 * @see ECDSA512AlgorithmFactory
 * @see RSA256AlgorithmFactory
 * @see RSA384AlgorithmFactory
 * @see RSA512AlgorithmFactory
 * @since 1.0.0
 */
public final class AlgorithmFactoryFactories {

    /**
     * 私有构造方法
     */
    private AlgorithmFactoryFactories() {
        super();
    }

    public static AlgorithmFactory none() {
        return Algorithm::none;
    }

    public static AlgorithmFactory hmac256(final String secret) {
        Asserts.hasText(secret);
        return () -> Algorithm.HMAC256(secret);
    }

    public static AlgorithmFactory hmac384(final String secret) {
        Asserts.hasText(secret);
        return () -> Algorithm.HMAC384(secret);
    }

    public static AlgorithmFactory hmac512(final String secret) {
        Asserts.hasText(secret);
        return () -> Algorithm.HMAC512(secret);
    }

    public static AlgorithmFactory rsa256(final String publicKey, final String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        return new RSA256AlgorithmFactory(publicKey, privateKey);
    }

    public static AlgorithmFactory rsa384(final String publicKey, final String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        return new RSA384AlgorithmFactory(publicKey, privateKey);
    }

    public static AlgorithmFactory rsa512(final String publicKey, final String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        return new RSA512AlgorithmFactory(publicKey, privateKey);
    }

    public static AlgorithmFactory ecdsa256(final String publicKey, final String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        return new ECDSA256AlgorithmFactory(publicKey, privateKey);
    }

    public static AlgorithmFactory ecdsa384(final String publicKey, final String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        return new ECDSA384AlgorithmFactory(publicKey, privateKey);
    }

    public static AlgorithmFactory ecdsa512(final String publicKey, final String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        return new ECDSA512AlgorithmFactory(publicKey, privateKey);
    }

}
