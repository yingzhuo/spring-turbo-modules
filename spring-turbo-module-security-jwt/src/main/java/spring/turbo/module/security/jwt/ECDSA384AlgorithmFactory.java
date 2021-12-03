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
import org.springframework.util.Assert;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class ECDSA384AlgorithmFactory extends ECDSAAlgorithmFactory {

    private final String publicKey;
    private final String privateKey;

    public ECDSA384AlgorithmFactory(String publicKey, String privateKey) {
        Assert.hasText(publicKey, "publicKey is blank");
        Assert.hasText(privateKey, "privateKey is blank");
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public Algorithm create() {
        return Algorithm.ECDSA384(toPublicKey(publicKey), toPrivateKey(privateKey));
    }

}
