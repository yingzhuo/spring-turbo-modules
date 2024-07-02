/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.misc;

import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.KeyStoreFormat;

import static spring.turbo.util.crypto.KeyStoreHelper.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class HutoolKeyStoreSignerFactoryBean implements FactoryBean<JWTSigner>, InitializingBean {

    private Resource resource;
    private KeyStoreFormat keyStoreFormat = KeyStoreFormat.PKCS12;
    private String storepass;
    private String alias;
    private String keypass;

    @Override
    public JWTSigner getObject() throws Exception {
        var ks = loadKeyStore(resource.getInputStream(), keyStoreFormat, storepass);
        var sigAlgName = getSigAlgName(ks, alias);
        var keyPair = getKeyPair(ks, alias, keypass);
        return JWTSignerUtil.createSigner(sigAlgName, keyPair);
    }

    @Override
    public Class<?> getObjectType() {
        return JWTSigner.class;
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.notNull(resource, "resource is null");
        Asserts.notNull(keyStoreFormat, "keyStoreFormat is null");
        Asserts.hasText(storepass, "storepass is null");
        Asserts.hasText(alias, "alias is null");
        Asserts.hasText(keypass, "keypass is null");
    }

    public void setResource(Resource resource) {
        this.resource = resource;
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
