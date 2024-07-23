package spring.turbo.module.jwt.alg;

import java.security.KeyPair;

/**
 * 封装了{@link KeyPair}的签名器
 *
 * @param keyPair 公私钥对
 * @author 应卓
 * @since 3.3.2
 */
public record KeyPairJwtSigner(KeyPair keyPair) implements JwtSigner {
}
