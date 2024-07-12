package spring.turbo.module.jwt.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import spring.turbo.util.crypto.pem.CertificatePemHelper;
import spring.turbo.util.crypto.pem.PemHelper;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class PemHutoolAsymmetricSignerFactoryBean extends HutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(PemHutoolAsymmetricSignerFactoryBean.class);

    private ResourceLoader resourceLoader;
    private String certificateLocation;
    private String keyLocation;

    /**
     * 默认构造方法
     */
    public PemHutoolAsymmetricSignerFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(certificateLocation, () -> "certificateLocation is required");
        Assert.notNull(keyLocation, () -> "keyLocation is required");

        var certResource = resourceLoader.getResource(certificateLocation);
        var keyResource = resourceLoader.getResource(keyLocation);

        try (var certInputStream = certResource.getInputStream();
             var keyInputStream = keyResource.getInputStream()) {

            var cert = CertificatePemHelper.readX509PemCertificate(certInputStream);
            var publicKey = cert.getPublicKey();
            var alg = publicKey.getAlgorithm();

            var factory = KeyFactory.getInstance(alg);
            var spec = new PKCS8EncodedKeySpec(PemHelper.readPemBytes(keyInputStream));
            var privateKey = factory.generatePrivate(spec);
            var sigAlgName = cert.getSigAlgName();

            logger.debug("asymmetric signer: {} ({})", alg, sigAlgName);

            super.setKeyPair(publicKey, privateKey);
            super.setSigAlgName(sigAlgName);
        }
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

}
