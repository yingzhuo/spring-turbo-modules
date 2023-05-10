/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.algorithm;

import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;
import spring.turbo.core.ResourceLoaders;
import spring.turbo.util.crypto.KeyStorage;

import java.security.KeyPair;

/**
 * JWT签名器生成工具
 *
 * @author 应卓
 *
 * @since 2.2.4
 */
public final class SignerFactories {

    /**
     * 私有构造方法
     */
    private SignerFactories() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JWTSigner none() {
        return JWTSignerUtil.none();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JWTSigner HS256(String key) {
        return JWTSignerUtil.hs256(key.getBytes());
    }

    public static JWTSigner HS384(String key) {
        return JWTSignerUtil.hs384(key.getBytes());
    }

    public static JWTSigner HS512(String key) {
        return JWTSignerUtil.hs512(key.getBytes());
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JWTSigner RS256(KeyPair keyPair) {
        var algId = "RS256";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner RS256(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("RSA", publicKeyIn, privateKeyIn);
        return RS256(keyPair);
    }

    public static JWTSigner RS384(KeyPair keyPair) {
        var algId = "RS384";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner RS384(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("RSA", publicKeyIn, privateKeyIn);
        return RS384(keyPair);
    }

    public static JWTSigner RS512(KeyPair keyPair) {
        var algId = "RS512";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner RS512(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("RSA", publicKeyIn, privateKeyIn);
        return RS512(keyPair);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JWTSigner ES256(KeyPair keyPair) {
        var algId = "ES256";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner ES256(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("EC", publicKeyIn, privateKeyIn);
        return ES256(keyPair);
    }

    public static JWTSigner ES384(KeyPair keyPair) {
        var algId = "ES384";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner ES384(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("EC", publicKeyIn, privateKeyIn);
        return ES384(keyPair);
    }

    public static JWTSigner ES512(KeyPair keyPair) {
        var algId = "ES512";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner ES512(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("EC", publicKeyIn, privateKeyIn);
        return ES512(keyPair);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JWTSigner PS256(KeyPair keyPair) {
        var algId = "PS256";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner PS256(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("RSA", publicKeyIn, privateKeyIn);
        return PS256(keyPair);
    }

    public static JWTSigner PS384(KeyPair keyPair) {
        var algId = "PS384";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner PS384(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("RSA", publicKeyIn, privateKeyIn);
        return PS384(keyPair);
    }

    public static JWTSigner PS512(KeyPair keyPair) {
        var algId = "PS512";
        return JWTSignerUtil.createSigner(algId, keyPair);
    }

    @SneakyThrows
    public static JWTSigner PS512(String publicKeyLocation, String privateKeyLocation) {
        var publicKeyIn = ResourceLoaders.getDefault().getResource(publicKeyLocation).getInputStream();
        var privateKeyIn = ResourceLoaders.getDefault().getResource(privateKeyLocation).getInputStream();
        var keyPair = KeyStorage.loadKeys("RSA", publicKeyIn, privateKeyIn);
        return PS512(keyPair);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static JWTSigner SM2(String publicKeyBase64, String privateKeyBase64) {
        return SM2(publicKeyBase64, privateKeyBase64, null);
    }

    public static JWTSigner SM2(String publicKeyBase64, String privateKeyBase64, @Nullable String withId) {
        var sm2 = SmUtil.sm2(privateKeyBase64, publicKeyBase64);
        return new SM2JWTSinger(sm2, withId);
    }

}
