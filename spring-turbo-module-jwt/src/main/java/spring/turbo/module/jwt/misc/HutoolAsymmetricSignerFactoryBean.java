package spring.turbo.module.jwt.misc;

import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * 通用非对称加密算法签名器
 *
 * @author 应卓
 * @see java.security.cert.Certificate
 * @see java.security.PrivateKey
 * @see java.security.PublicKey
 * @since 3.3.1
 */
public abstract class HutoolAsymmetricSignerFactoryBean implements FactoryBean<JWTSigner> {

    @Nullable
    private String sigAlgName;

    @Nullable
    private KeyPair keyPair;

    /**
     * 默认构造方法
     */
    public HutoolAsymmetricSignerFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JWTSigner getObject() {
        return JWTSignerUtil.createSigner(sigAlgName, keyPair);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Class<?> getObjectType() {
        return JWTSigner.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isSingleton() {
        return true;
    }

    public final void setSigAlgName(String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    public final void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public final void setKeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.keyPair = new KeyPair(publicKey, privateKey);
    }

    public final void setKeyPairAndSigAlgName(Certificate certificate, PrivateKey privateKey) {
        this.keyPair = new KeyPair(certificate.getPublicKey(), privateKey);
        this.sigAlgName = ((X509Certificate) certificate).getSigAlgName();
    }

}
