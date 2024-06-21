/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.signer.delegate;

import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.hutool.jwt.signers.NoneJWTSigner;
import org.springframework.lang.Nullable;
import spring.turbo.module.jwt.misc.HutoolSM2JWTSinger;
import spring.turbo.module.jwt.signer.JsonWebTokenSigner;
import spring.turbo.util.Asserts;

import java.security.KeyPair;

/**
 * @author 应卓
 * @since 3.3.1
 */
public final class HutoolJsonWebTokenSigner implements JsonWebTokenSigner {

    private final JWTSigner delegating;

    private HutoolJsonWebTokenSigner(JWTSigner delegating) {
        Asserts.notNull(delegating, "delegating is required");
        this.delegating = delegating;
    }

    public static HutoolJsonWebTokenSigner of(JWTSigner signer) {
        return new HutoolJsonWebTokenSigner(signer);
    }

    public static HutoolJsonWebTokenSigner none() {
        return of(NoneJWTSigner.NONE);
    }

    public static HutoolJsonWebTokenSigner HS256(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs256(key.getBytes()));
    }

    public static HutoolJsonWebTokenSigner HS384(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs384(key.getBytes()));
    }

    public static HutoolJsonWebTokenSigner HS512(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs512(key.getBytes()));
    }

    public static HutoolJsonWebTokenSigner RS256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner RS384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner RS512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner ES256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner ES384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner ES512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner PS256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner PS384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner PS512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner SM2(String publicKeyBase64, String privateKeyBase64) {
        return SM2(publicKeyBase64, privateKeyBase64, null);
    }

    public static HutoolJsonWebTokenSigner SM2(String publicKeyBase64, String privateKeyBase64, @Nullable String withId) {
        var sm2 = SmUtil.sm2(privateKeyBase64, publicKeyBase64);
        return of(new HutoolSM2JWTSinger(sm2, withId));
    }

    public static HutoolJsonWebTokenSigner RSHA1(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RSHA1";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static HutoolJsonWebTokenSigner RMD2(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RMD2";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static HutoolJsonWebTokenSigner RMD5(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RMD5";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sign(String headerBase64, String payloadBase64) {
        return this.delegating.sign(headerBase64, payloadBase64);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
        try {
            return this.delegating.verify(headerBase64, payloadBase64, signBase64);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlgorithm() {
        return this.delegating.getAlgorithmId();
    }

}
