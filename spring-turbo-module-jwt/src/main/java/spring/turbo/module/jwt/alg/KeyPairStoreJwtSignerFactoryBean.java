package spring.turbo.module.jwt.alg;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import spring.turbo.util.crypto.bundle.KeyStoreAsymmetricKeyBundleFactoryBean;
import spring.turbo.util.crypto.keystore.KeyStoreFormat;

/**
 * @author 应卓
 * @see KeyPairJwtSigner
 * @since 3.3.2
 */
public class KeyPairStoreJwtSignerFactoryBean implements FactoryBean<KeyPairJwtSigner>, InitializingBean {

    private final KeyStoreAsymmetricKeyBundleFactoryBean innerFactory = new KeyStoreAsymmetricKeyBundleFactoryBean();

    /**
     * 默认构造方法
     */
    public KeyPairStoreJwtSignerFactoryBean() {
        this.setFormat(KeyStoreFormat.PKCS12);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPairJwtSigner getObject() throws Exception {
        innerFactory.afterPropertiesSet();
        var bundle = innerFactory.getObject();
        return new KeyPairJwtSigner(bundle.getKeyPair());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.innerFactory.afterPropertiesSet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return KeyPairJwtSigner.class;
    }

    public void setLocation(String keyStoreLocation) {
        innerFactory.setLocation(keyStoreLocation);
    }

    public void setFormat(KeyStoreFormat keyStoreFormat) {
        innerFactory.setFormat(keyStoreFormat);
    }

    public void setStorepass(String storepass) {
        innerFactory.setStorepass(storepass);
    }

    public void setAlias(String alias) {
        innerFactory.setAlias(alias);
    }

    public void setKeypass(String keypass) {
        innerFactory.setKeypass(keypass);
    }

}
