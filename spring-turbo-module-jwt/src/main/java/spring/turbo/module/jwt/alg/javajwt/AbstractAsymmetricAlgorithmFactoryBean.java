package spring.turbo.module.jwt.alg.javajwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 通用非对称加密算法签名器，生成{@link Algorithm}实例
 *
 * @author 应卓
 * @see java.security.cert.Certificate
 * @see java.security.PrivateKey
 * @see java.security.PublicKey
 * @since 3.3.2
 */
abstract class AbstractAsymmetricAlgorithmFactoryBean implements FactoryBean<Algorithm> {

    @Nullable
    private String sigAlgName;

    @Nullable
    private KeyPair keyPair;

    /**
     * {@inheritDoc}
     */
    @Override
    public final Algorithm getObject() {
        Assert.hasText(sigAlgName, "sigAlgName is null or blank");
        Assert.notNull(keyPair, "keyPair is required");

        if ("SHA256withRSA".equalsIgnoreCase(this.sigAlgName)) {
            return Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
        }

        if ("SHA384withRSA".equalsIgnoreCase(this.sigAlgName)) {
            return Algorithm.RSA384((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
        }

        if ("SHA512withRSA".equalsIgnoreCase(this.sigAlgName)) {
            return Algorithm.RSA512((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
        }

        if ("SHA256withECDSA".equalsIgnoreCase(this.sigAlgName)) {
            return Algorithm.ECDSA256((ECPublicKey) keyPair.getPublic(), (ECPrivateKey) keyPair.getPrivate());
        }

        if ("SHA384withECDSA".equalsIgnoreCase(this.sigAlgName)) {
            return Algorithm.ECDSA384((ECPublicKey) keyPair.getPublic(), (ECPrivateKey) keyPair.getPrivate());
        }

        if ("SHA512withECDSA".equalsIgnoreCase(this.sigAlgName)) {
            return Algorithm.ECDSA512((ECPublicKey) keyPair.getPublic(), (ECPrivateKey) keyPair.getPrivate());
        }

        var msg = String.format("'%s' is not supported yet", sigAlgName);
        throw new IllegalStateException(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Class<?> getObjectType() {
        return Algorithm.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isSingleton() {
        return true;
    }

    protected final void setSigAlgName(@Nullable String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    protected final void setKeyPair(@Nullable KeyPair keyPair) {
        this.keyPair = keyPair;
    }

}
