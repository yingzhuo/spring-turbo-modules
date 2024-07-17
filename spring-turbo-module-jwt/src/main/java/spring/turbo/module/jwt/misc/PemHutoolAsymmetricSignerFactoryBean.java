package spring.turbo.module.jwt.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import spring.turbo.util.crypto.pem.PemReadingUtils;

import java.security.PrivateKey;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class PemHutoolAsymmetricSignerFactoryBean extends HutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(PemHutoolAsymmetricSignerFactoryBean.class);

    @NonNull
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @NonNull
    private String certificateLocation;

    @NonNull
    private String keyLocation;

    @Nullable
    private String keyPassword;

    /**
     * 默认构造方法
     */
    public PemHutoolAsymmetricSignerFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(certificateLocation, () -> "certificateLocation is required");
        Assert.notNull(keyLocation, () -> "keyLocation is required");

        var certResource = resourceLoader.getResource(certificateLocation);
        var keyResource = resourceLoader.getResource(keyLocation);
        var cert = PemReadingUtils.readX509PemCertificate(certResource);
        var privateKey = (PrivateKey) PemReadingUtils.readPkcs8Key(keyResource, keyPassword);
        var alg = privateKey.getAlgorithm();
        var sigAlgName = cert.getSigAlgName();
        logger.debug("asymmetric signer: {} ({})", alg, sigAlgName);
        super.setKeyPair(cert.getPublicKey(), privateKey);
        super.setSigAlgName(sigAlgName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setCertificateLocation(String certificateLocation) {
        this.certificateLocation = certificateLocation;
    }

    public void setKeyLocation(String keyLocation) {
        this.keyLocation = keyLocation;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

}
