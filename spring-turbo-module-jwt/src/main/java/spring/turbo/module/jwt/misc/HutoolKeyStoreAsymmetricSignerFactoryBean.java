package spring.turbo.module.jwt.misc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import spring.turbo.util.crypto.bundle.KeyStoreAsymmetricKeyBundleFactoryBean;
import spring.turbo.util.crypto.keystore.KeyStoreFormat;

import java.security.KeyStore;

/**
 * {@link AbstractHutoolAsymmetricSignerFactoryBean} 的子类型，从KeyStore加载秘钥等。建议使用 PKCS#12 格式。
 *
 * @author 应卓
 * @see KeyStore
 * @since 3.3.1
 */
public class HutoolKeyStoreAsymmetricSignerFactoryBean extends AbstractHutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private final KeyStoreAsymmetricKeyBundleFactoryBean delegatingFactory = new KeyStoreAsymmetricKeyBundleFactoryBean();

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        delegatingFactory.afterPropertiesSet();

        var bundle = delegatingFactory.getObject();
        if (bundle == null) {
            throw new IllegalStateException("cannot load bundle");
        }

        super.setKeyPairAndSigAlgName(bundle.getCertificate(), bundle.getPrivateKey());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        delegatingFactory.setResourceLoader(resourceLoader);
    }

    public void setLocation(String location) {
        delegatingFactory.setLocation(location);
    }

    public void setFormat(KeyStoreFormat format) {
        delegatingFactory.setFormat(format);
    }

    public void setStorepass(String storepass) {
        delegatingFactory.setStorepass(storepass);
    }

    public void setAlias(String alias) {
        delegatingFactory.setAlias(alias);
    }

    public void setKeypass(String keypass) {
        delegatingFactory.setKeypass(keypass);
    }

}
