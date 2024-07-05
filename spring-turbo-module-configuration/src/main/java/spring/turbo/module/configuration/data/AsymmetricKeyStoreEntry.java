package spring.turbo.module.configuration.data;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * 非对称加密算法秘钥条目
 *
 * @author 应卓
 * @since 3.3.1
 */
public interface AsymmetricKeyStoreEntry extends KeyStoreEntry {

    /**
     * 获取签名算法名称
     *
     * @return 签名算法名称
     */
    public default String sigAlgName() {
        return ((X509Certificate) certificate()).getSigAlgName();
    }

    /**
     * 获取签名算法OID
     *
     * @return 签名算法OID
     */
    public default String sigAlgOID() {
        return ((X509Certificate) certificate()).getSigAlgOID();
    }

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public KeyPair keyPair();

    /**
     * 获取公钥
     *
     * @return 公钥
     */
    public default PublicKey publicKey() {
        return keyPair().getPublic();
    }

    /**
     * 获取私钥
     *
     * @return 私钥
     */
    public default PrivateKey privateKey() {
        return keyPair().getPrivate();
    }

    /**
     * 获取证书
     *
     * @return 获取证书
     */
    public Certificate certificate();

    /**
     * {@inheritDoc}
     */
    @Override
    public default String algName() {
        return privateKey().getAlgorithm();
    }

}
