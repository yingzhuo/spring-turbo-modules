package spring.turbo.module.jwt.alg;

import org.springframework.beans.factory.FactoryBean;
import spring.turbo.util.crypto.keystore.KeyStoreFormat;

import static spring.turbo.core.ResourceUtils.loadResource;
import static spring.turbo.util.crypto.keystore.KeyStoreHelper.getKeyPair;
import static spring.turbo.util.crypto.keystore.KeyStoreHelper.loadKeyStore;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class KeyPairStoreJwtSignerFactoryBean implements FactoryBean<KeyPairJwtSigner> {

    private String keyStoreLocation;
    private KeyStoreFormat keyStoreFormat = KeyStoreFormat.PKCS12;
    private String storepass;
    private String alias;
    private String keypass;

    @Override
    public KeyPairJwtSigner getObject() throws Exception {
        try (var inputStream = loadResource(keyStoreLocation).getInputStream()) {
            var ks = loadKeyStore(inputStream, keyStoreFormat, storepass);
            var keyPair = getKeyPair(ks, alias, keypass);
            return new KeyPairJwtSigner(keyPair);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return KeyPairJwtSigner.class;
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
