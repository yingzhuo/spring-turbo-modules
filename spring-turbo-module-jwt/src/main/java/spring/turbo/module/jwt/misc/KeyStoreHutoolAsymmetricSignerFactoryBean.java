package spring.turbo.module.jwt.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import spring.turbo.util.crypto.keystore.KeyStoreFormat;

import java.security.cert.X509Certificate;

import static spring.turbo.util.crypto.keystore.KeyStoreHelper.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class KeyStoreHutoolAsymmetricSignerFactoryBean extends AbstractHutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(KeyStoreHutoolAsymmetricSignerFactoryBean.class);

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String location;
    private KeyStoreFormat format = KeyStoreFormat.PKCS12;
    private String storepass;
    private String alias;
    private String keypass;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(location, () -> "location is required");
        Assert.notNull(format, () -> "format is required");
        Assert.notNull(storepass, () -> "storepass is required");
        Assert.notNull(alias, () -> "alias is required");
        Assert.notNull(keypass, () -> "keypass is required");

        var resource = resourceLoader.getResource(location);
        try (var inputStream = resource.getInputStream()) {
            var store = loadKeyStore(inputStream, format, storepass);
            var cert = getCertificate(store, alias);
            var privateKey = getPrivateKey(store, alias, keypass);
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFormat(KeyStoreFormat format) {
        this.format = format;
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
