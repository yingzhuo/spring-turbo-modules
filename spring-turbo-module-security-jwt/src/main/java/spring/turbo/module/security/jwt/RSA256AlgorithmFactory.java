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
 * @since 1.0.0
 */
final class RSA256AlgorithmFactory extends RSAAlgorithmFactory {

    private final String publicKey;
    private final String privateKey;

    public RSA256AlgorithmFactory(String publicKey, String privateKey) {
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public Algorithm create() {
        return Algorithm.RSA256(toPublicKey(publicKey), toPrivateKey(privateKey));
    }

}
