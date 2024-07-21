package spring.turbo.module.jwt.misc.javajwt;

import org.springframework.beans.factory.InitializingBean;
import spring.turbo.util.crypto.bundle.KeyStoreAsymmetricKeyBundleFactoryBean;
import spring.turbo.util.crypto.keystore.KeyStoreFormat;

/**
 * {@link AbstractAsymmetricAlgorithmFactoryBean} 的子类型，从KeyStore文件中加载秘钥。
 *
 * @author 应卓
 * @since 3.3.2
 */
public class KeyStoreAsymmetricAlgorithmFactoryBean extends AbstractAsymmetricAlgorithmFactoryBean
        implements InitializingBean {

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

        super.setKeyPair(bundle.getKeyPair());
        super.setSigAlgName(bundle.getSigAlgName());
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
