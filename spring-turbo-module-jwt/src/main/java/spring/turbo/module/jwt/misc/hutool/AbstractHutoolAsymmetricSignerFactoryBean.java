package spring.turbo.module.jwt.misc.hutool;

import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.security.KeyPair;

/**
 * 通用非对称加密算法签名器，生成{@link JWTSigner}实例
 *
 * @author 应卓
 * @see java.security.cert.Certificate
 * @see java.security.PrivateKey
 * @see java.security.PublicKey
 * @since 3.3.1
 */
abstract class AbstractHutoolAsymmetricSignerFactoryBean implements FactoryBean<JWTSigner> {

    @Nullable
    private String sigAlgName;

    @Nullable
    private KeyPair keyPair;

    /**
     * {@inheritDoc}
     */
    @Override
    public final JWTSigner getObject() {
        Assert.hasText(sigAlgName, "sigAlgName is null or blank");
        Assert.notNull(keyPair, "keyPair is required");

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

}
