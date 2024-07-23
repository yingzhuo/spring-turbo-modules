package spring.turbo.module.jwt.alg;

import java.io.Serializable;

/**
 * JWT签名算法 <br>
 * 标记型接口
 *
 * @author 应卓
 * @see KeyPairJwtSigner
 * @see SecretKeyJwtSigner
 * @since 3.3.2
 */
public sealed interface JwtSigner extends Serializable permits KeyPairJwtSigner, SecretKeyJwtSigner {
}
