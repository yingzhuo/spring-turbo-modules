package spring.turbo.module.jwt.alg.javajwt;

import org.springframework.beans.factory.InitializingBean;
import spring.turbo.util.crypto.bundle.PemAsymmetricKeyBundleFactoryBean;

/**
 * {@link AbstractAsymmetricAlgorithmFactoryBean} 的子类型，从PEM文件中加载秘钥。
 *
 * @author 应卓
 * @since 3.3.2
 */
public class PemAsymmetricAlgorithmFactoryBean extends AbstractAsymmetricAlgorithmFactoryBean
        implements InitializingBean {

    private final PemAsymmetricKeyBundleFactoryBean delegatingFactory = new PemAsymmetricKeyBundleFactoryBean();

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
