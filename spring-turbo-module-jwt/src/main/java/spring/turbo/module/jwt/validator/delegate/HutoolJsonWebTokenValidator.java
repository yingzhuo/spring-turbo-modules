/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.validator.delegate;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.lang.Nullable;
import spring.turbo.module.jwt.misc.HutoolSM2JWTSinger;
import spring.turbo.module.jwt.validator.JsonWebTokenValidator;
import spring.turbo.util.Asserts;

import java.security.KeyPair;

import static spring.turbo.module.jwt.validator.JsonWebTokenValidator.Result.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class HutoolJsonWebTokenValidator implements JsonWebTokenValidator {

    private final JWTSigner signer;

    public static HutoolJsonWebTokenValidator of(JWTSigner jwtSigner) {
        return new HutoolJsonWebTokenValidator(jwtSigner);
    }

    public static JsonWebTokenValidator HS256(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs256(key.getBytes()));
    }

    public static JsonWebTokenValidator HS384(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs384(key.getBytes()));
    }

    public static JsonWebTokenValidator H512(String key) {
        Asserts.hasText(key, "key is null or blank");
        return of(JWTSignerUtil.hs512(key.getBytes()));
    }

    public static JsonWebTokenValidator RS256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator RS384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator RS512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RS512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator ES256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator ES384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator ES512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "ES512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator PS256(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS256";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator PS384(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS384";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator PS512(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "PS512";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator SM2(String publicKeyBase64, String privateKeyBase64) {
        return SM2(publicKeyBase64, privateKeyBase64, null);
    }

    public static JsonWebTokenValidator SM2(String publicKeyBase64, String privateKeyBase64, @Nullable String withId) {
        var sm2 = SmUtil.sm2(privateKeyBase64, publicKeyBase64);
        return of(new HutoolSM2JWTSinger(sm2, withId));
    }

    public static JsonWebTokenValidator RSHA1(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RSHA1";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator RMD2(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RMD2";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    public static JsonWebTokenValidator RMD5(KeyPair keyPair) {
        Asserts.notNull(keyPair, "keyPair is null");
        var algId = "RMD5";
        return of(JWTSignerUtil.createSigner(algId, keyPair));
    }

    private HutoolJsonWebTokenValidator(JWTSigner signer) {
        Asserts.notNull(signer, "signer is required");
        this.signer = signer;
    }

    @Override
    public Result validate(String token) {
        JWT jwt;

        try {
            jwt = JWT.of(token);
        } catch (Exception e) {
            return INVALID_JWT_FORMAT;
        }

        var validator = JWTValidator.of(jwt);

        try {
            validator.validateAlgorithm(signer);
        } catch (ValidateException e) {
            return INVALID_SIGNATURE;
        }

        try {
            validator.validateDate();
        } catch (ValidateException e) {
            return INVALID_TIME;
        }

        return NO_PROBLEM;
    }

}
