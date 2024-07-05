package spring.turbo.module.jwt.factory.delegate;

import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.hutool.jwt.signers.NoneJWTSigner;
import org.springframework.lang.Nullable;
import spring.turbo.module.jwt.factory.JsonWebTokenData;
import spring.turbo.module.jwt.factory.JsonWebTokenFactory;
import spring.turbo.module.jwt.misc.HutoolSM2Signer;
import spring.turbo.util.Asserts;

import java.security.KeyPair;

/**
 * Hutool工具库装饰器实现
 *
 * @author 应卓
 * @see <a href="https://hutool.cn/docs/#/jwt/%E6%A6%82%E8%BF%B0">Hutool官方文档</a>
 * @since 3.3.1
 */
public class HutoolJsonWebTokenFactory implements JsonWebTokenFactory {

    /**
     * Hutool 签名器
     */
    private final JWTSigner signer;

    /**
     * 构造方法
     *
     * @param signer hutool签名器
     */
    private HutoolJsonWebTokenFactory(JWTSigner signer) {
        this.signer = signer;
    }

    public static HutoolJsonWebTokenFactory of(JWTSigner signer) {
        return new HutoolJsonWebTokenFactory(signer);
    }

    public static HutoolJsonWebTokenFactory none() {
        return of(NoneJWTSigner.NONE);
    }

    public static HutoolJsonWebTokenFactory HS256(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs256(key.getBytes()));
    }

    public static HutoolJsonWebTokenFactory HS384(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs384(key.getBytes()));
    }

    public static HutoolJsonWebTokenFactory HS512(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs512(key.getBytes()));
    }

    public static HutoolJsonWebTokenFactory RS256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory RS384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory RS512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory ES256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory ES384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory ES512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory PS256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory PS384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory PS512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory SM2(String publicKeyBase64, String privateKeyBase64) {
        return SM2(publicKeyBase64, privateKeyBase64, null);
    }

    public static HutoolJsonWebTokenFactory SM2(String publicKeyBase64, String privateKeyBase64, @Nullable String withId) {
        var sm2 = SmUtil.sm2(privateKeyBase64, publicKeyBase64);
        return of(new HutoolSM2Signer(sm2, withId));
    }

    public static HutoolJsonWebTokenFactory RSHA1(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RSHA1";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory RMD2(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RMD2";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenFactory RMD5(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RMD5";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    @Override
    public String apply(JsonWebTokenData data) {
        return JWTUtil.createToken(data.getPayloadMap(), this.signer);
    }

}
