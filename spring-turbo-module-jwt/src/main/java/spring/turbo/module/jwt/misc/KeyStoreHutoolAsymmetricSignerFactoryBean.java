package spring.turbo.module.jwt.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import spring.turbo.util.crypto.KeyStoreFormat;

import java.security.cert.X509Certificate;

import static spring.turbo.util.crypto.KeyStoreHelper.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class KeyStoreHutoolAsymmetricSignerFactoryBean extends HutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(KeyStoreHutoolAsymmetricSignerFactoryBean.class);

    private ResourceLoader resourceLoader;
    private String keyStoreLocation;
    private KeyStoreFormat keyStoreFormat = KeyStoreFormat.PKCS12;
    private String storepass;
    private String alias;
    private String keypass;

    /**
     * 默认构造方法
     */
    public KeyStoreHutoolAsymmetricSignerFactoryBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(keyStoreLocation, () -> "keyStoreLocation is required");
        Assert.notNull(keyStoreFormat, () -> "keyStoreFormat is required");
        Assert.notNull(storepass, () -> "storepass is required");
        Assert.notNull(alias, () -> "alias is required");
        Assert.notNull(keypass, () -> "keypass is required");

        var keyStoreResource = resourceLoader.getResource(keyStoreLocation);

        try (var keyStoreInputStream = keyStoreResource.getInputStream()) {
            var ks = loadKeyStore(keyStoreInputStream, keyStoreFormat, storepass);

            var cert = getCertificate(ks, alias);
            var privateKey = getPrivateKey(ks, alias, keypass);

            logger.debug("asymmetric signer: {} ({})", privateKey.getAlgorithm(), ((X509Certificate) cert).getSigAlgName());

            super.setKeyPairAndSigAlgName(cert, privateKey);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setKeyStoreLocation(String keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public void setKeyStoreFormat(KeyStoreFormat keyStoreFormat) {
        this.keyStoreFormat = keyStoreFormat;
    }

    public void setStorepass(String storepass) {
        this.storepass = storepass;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setKeypass(String keypass) {
        this.keypass = keypass;
    }

}
