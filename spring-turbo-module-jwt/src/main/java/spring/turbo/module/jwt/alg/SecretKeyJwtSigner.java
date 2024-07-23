package spring.turbo.module.jwt.alg;

import javax.crypto.SecretKey;

/**
 * 封装了{@link SecretKey}的签名器
 *
 * @param secretKey 秘钥
 * @author 应卓
 * @since 3.3.2
 */
public record SecretKeyJwtSigner(SecretKey secretKey) implements JwtSigner {
}
