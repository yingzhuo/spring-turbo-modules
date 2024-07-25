package spring.turbo.module.jwt.alg;

import org.springframework.beans.factory.FactoryBean;
import spring.turbo.util.crypto.bundle.PemAsymmetricKeyBundleFactoryBean;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class KeyPairPemJwtSignerFactoryBean implements FactoryBean<KeyPairJwtSigner> {

    private final PemAsymmetricKeyBundleFactoryBean innerFactory = new PemAsymmetricKeyBundleFactoryBean();

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPairJwtSigner getObject() {
        innerFactory.afterPropertiesSet();
        var bundle = innerFactory.getObject();
        return new KeyPairJwtSigner(bundle.getKeyPair());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return KeyPairJwtSigner.class;
    }

    public void setCertificateLocation(String certificateLocation) {
        innerFactory.setCertificateLocation(certificateLocation);
    }

    public void setPrivateKeyLocation(String privateKeyLocation) {
        innerFactory.setPrivateKeyLocation(privateKeyLocation);
    }

    public void setCertificateContent(String certificateContent) {
        innerFactory.setCertificateContent(certificateContent);
    }

    public void setPrivateKeyContent(String privateKeyContent) {
        innerFactory.setPrivateKeyContent(privateKeyContent);
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        innerFactory.setPrivateKeyPassword(privateKeyPassword);
    }

}
