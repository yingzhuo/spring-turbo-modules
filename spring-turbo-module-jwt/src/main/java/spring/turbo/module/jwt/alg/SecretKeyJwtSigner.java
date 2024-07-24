package spring.turbo.module.jwt.alg;

import org.springframework.core.style.ToStringCreator;

import javax.crypto.SecretKey;

/**
 * 封装了{@link SecretKey}的签名器
 *
 * @param secretKey 秘钥
 * @author 应卓
 * @since 3.3.2
 */
public record SecretKeyJwtSigner(SecretKey secretKey) implements JwtSigner {

    @Override
    public String toString() {
        var creator = new ToStringCreator(this);
        return creator.toString();
    }

}
