package spring.turbo.module.jwt.alg;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.lang.Nullable;
import spring.turbo.util.crypto.keystore.KeyStoreHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.KeyPair;

import static java.nio.charset.StandardCharsets.UTF_8;
import static spring.turbo.core.ResourceUtils.loadResource;
import static spring.turbo.util.crypto.keystore.KeyStoreFormat.PKCS12;
import static spring.turbo.util.crypto.keystore.KeyStoreHelper.getCertificate;
import static spring.turbo.util.crypto.keystore.KeyStoreHelper.loadKeyStore;
import static spring.turbo.util.crypto.pem.PemReadingUtils.readPkcs8PrivateKey;
import static spring.turbo.util.crypto.pem.PemReadingUtils.readX509Certificate;

/**
 * {@link JwtSigner} 生成工具
 *
 * @author 应卓
 * @since 3.3.2
 */
public final class JwtSignerFactories {

    /**
     * 私有构造方法
     */
    private JwtSignerFactories() {
    }

    /**
     * 从Base64编码的数据中加载秘钥
     *
     * @param encodedString Base64编码的数据
     * @return 签名器实例
     */
    public static SecretKeyJwtSigner createFromBase64EncodedString(String encodedString) {
        var sk = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedString));
        return new SecretKeyJwtSigner(sk);
    }

    /**
     * 从PEM文件中加载
     *
     * @param certificateLocation 证书
     * @param privateKeyLocation  私钥
     * @param privateKeyPassword  私钥密码，或null
     * @return 签名器实例
     */
    public static KeyPairJwtSigner createFromPemResource(
            String certificateLocation,
            String privateKeyLocation,
            @Nullable String privateKeyPassword) {

        var certResource = loadResource(certificateLocation);
        var privateKeyResource = loadResource(privateKeyLocation);

        try {
            return createFromPemContent(
                    certResource.getContentAsString(UTF_8),
                    privateKeyResource.getContentAsString(UTF_8),
                    privateKeyPassword
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 从PEM内容中加载
     *
     * @param certificateContent 证书
     * @param privateKeyContent  私钥
     * @param privateKeyPassword 私钥密码，或null
     * @return 签名器实例
     */
    public static KeyPairJwtSigner createFromPemContent(
            String certificateContent,
            String privateKeyContent,
            @Nullable String privateKeyPassword
    ) {
        var cert = readX509Certificate(certificateContent);
        var privateKey = readPkcs8PrivateKey(privateKeyContent, privateKeyPassword);

        return new KeyPairJwtSigner(
                new KeyPair(cert.getPublicKey(), privateKey)
        );
    }

    /**
     * 送PKCS#12文件中加载
     *
     * @param storeLocation p12文件
     * @param storepass     store密码
     * @param alias         别名
     * @param keypass       私钥密码
     * @return 签名器实例
     */
    public static KeyPairJwtSigner createFromP12Store(
            String storeLocation,
            String storepass,
            String alias,
            String keypass
    ) {
        var resource = loadResource(storeLocation);

        try (var inputStream = resource.getInputStream()) {
            var store = loadKeyStore(inputStream, PKCS12, storepass);
            var cert = getCertificate(store, alias);
            var privateKey = KeyStoreHelper.getPrivateKey(store, alias, keypass);

            return new KeyPairJwtSigner(
                    new KeyPair(cert.getPublicKey(), privateKey)
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
