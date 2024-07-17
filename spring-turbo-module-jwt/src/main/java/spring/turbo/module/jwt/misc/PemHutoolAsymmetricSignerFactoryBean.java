package spring.turbo.module.jwt.misc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import spring.turbo.util.crypto.bundle.PemAsymmetricKeyBundleFactoryBean;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class PemHutoolAsymmetricSignerFactoryBean extends AbstractHutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private final PemAsymmetricKeyBundleFactoryBean delegatingFactory = new PemAsymmetricKeyBundleFactoryBean();

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        delegatingFactory.setResourceLoader(resourceLoader);
    }

    public void setCertificateLocation(String certificateLocation) {
        delegatingFactory.setCertificateLocation(certificateLocation);
    }

    public void setKeyLocation(String keyLocation) {
        delegatingFactory.setKeyLocation(keyLocation);
    }

    public void setKeyPassword(String keyPassword) {
        delegatingFactory.setKeyPassword(keyPassword);
    }

    public void setCertificateContent(String certificateContent) {
        delegatingFactory.setCertificateContent(certificateContent);
    }

    public void setKeyContent(String keyContent) {
        delegatingFactory.setKeyContent(keyContent);
    }

}
