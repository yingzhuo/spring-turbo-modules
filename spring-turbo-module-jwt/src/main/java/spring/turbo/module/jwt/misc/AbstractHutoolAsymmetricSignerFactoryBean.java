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
public abstract class AbstractHutoolAsymmetricSignerFactoryBean implements FactoryBean<JWTSigner> {

    @Nullable
    private String sigAlgName;

    @Nullable
    private KeyPair keyPair;

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

    protected final void setSigAlgName(String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    protected final void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    protected final void setKeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.keyPair = new KeyPair(publicKey, privateKey);
    }

    protected final void setKeyPairAndSigAlgName(Certificate certificate, PrivateKey privateKey) {
        this.keyPair = new KeyPair(certificate.getPublicKey(), privateKey);
        this.sigAlgName = ((X509Certificate) certificate).getSigAlgName();
    }

}
