/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.algorithm;

import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.io.ResourceUtils;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @see HutoolSignAlgorithm
 * @since 2.2.4
 */
public final class EnvironmentHutoolSignAlgorithmFactoryBean implements FactoryBean<HutoolSignAlgorithm>, EnvironmentAware {

    @NonNull
    private Environment environment = new StandardEnvironment();

    private String privateKeyEnvironmentName = "HUTOOL_JWT_ALG_PRIVATE_KEY";
    private String publicKeyEnvironmentName = "HUTOOL_JWT_ALG_PUBLIC_KEY";

    @Nullable
    private SignAlgorithm signAlgorithm;

    @Nullable
    private Resource defaultPrivateKey;

    @Nullable
    private Resource defaultPublicKey;

    /**
     * 默认构造方法
     */
    public EnvironmentHutoolSignAlgorithmFactoryBean() {
        super();
    }

    @Override
    public HutoolSignAlgorithm getObject() {
        Asserts.notNull(signAlgorithm, "signAlgorithm not set");
        var publicKey = getPublicKey();
        var privateKey = getPrivateKey();
        return new HutoolSignAlgorithm(signAlgorithm, publicKey, privateKey);
    }

    @Override
    public Class<?> getObjectType() {
        return HutoolSignAlgorithm.class;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private String getPrivateKey() {
        var key = environment.getProperty(privateKeyEnvironmentName);

        if (key == null && defaultPrivateKey != null) {
            key = ResourceUtils.readText(defaultPrivateKey);
        }

        Asserts.notNull(key, "Unable get private key.");
        return key;
    }

    private String getPublicKey() {
        var key = environment.getProperty(publicKeyEnvironmentName);

        if (key == null && defaultPublicKey != null) {
            key = ResourceUtils.readText(defaultPublicKey);
        }

        Asserts.notNull(key, "Unable get public key.");
        return key;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void setPrivateKeyEnvironmentName(String privateKeyEnvironmentName) {
        this.privateKeyEnvironmentName = privateKeyEnvironmentName;
    }

    public void setPublicKeyEnvironmentName(String publicKeyEnvironmentName) {
        this.publicKeyEnvironmentName = publicKeyEnvironmentName;
    }

    public void setSignAlgorithm(SignAlgorithm signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public void setDefaultPrivateKey(Resource defaultPrivateKey) {
        this.defaultPrivateKey = defaultPrivateKey;
    }

    public void setDefaultPublicKey(Resource defaultPublicKey) {
        this.defaultPublicKey = defaultPublicKey;
    }

}
