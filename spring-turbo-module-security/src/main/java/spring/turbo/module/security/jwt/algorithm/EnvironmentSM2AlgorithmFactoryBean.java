/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.algorithm;

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
 * @see SM2Algorithm
 * @since 2.2.4
 */
public final class EnvironmentSM2AlgorithmFactoryBean implements FactoryBean<SM2Algorithm>, EnvironmentAware {

    @NonNull
    private Environment environment = new StandardEnvironment();

    private String privateKeyEnvironmentName = "SM2_JWT_ALG_PRIVATE_KEY";
    private String publicKeyEnvironmentName = "SM2_JWT_ALG_PUBLIC_KEY";
    private String withIdEnvironmentName = "SM2_JWT_ALG_WITH_ID";

    @Nullable
    private Resource defaultPrivateKey;

    @Nullable
    private Resource defaultPublicKey;

    @Nullable
    private Resource defaultWithId;

    /**
     * 默认构造方法
     */
    public EnvironmentSM2AlgorithmFactoryBean() {
        super();
    }

    @Override
    public SM2Algorithm getObject() {
        var publicKey = getPublicKey();
        var privateKey = getPrivateKey();
        var withId = getWithId();
        return new SM2Algorithm(publicKey, privateKey, withId);
    }

    @Override
    public Class<?> getObjectType() {
        return SM2Algorithm.class;
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

    @Nullable
    private String getWithId() {
        var id = environment.getProperty(withIdEnvironmentName);

        if (id == null) {
            if (defaultWithId != null) {
                id = ResourceUtils.readText(defaultWithId);
            }
        }

        return id;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public void setPrivateKeyEnvironmentName(String privateKeyEnvironmentName) {
        this.privateKeyEnvironmentName = privateKeyEnvironmentName;
    }

    public void setPublicKeyEnvironmentName(String publicKeyEnvironmentName) {
        this.publicKeyEnvironmentName = publicKeyEnvironmentName;
    }

    public void setWithIdEnvironmentName(String withIdEnvironmentName) {
        this.withIdEnvironmentName = withIdEnvironmentName;
    }

    public void setDefaultPrivateKey(@Nullable Resource defaultPrivateKey) {
        this.defaultPrivateKey = defaultPrivateKey;
    }

    public void setDefaultPublicKey(@Nullable Resource defaultPublicKey) {
        this.defaultPublicKey = defaultPublicKey;
    }

    public void setDefaultWithId(@Nullable Resource defaultWithId) {
        this.defaultWithId = defaultWithId;
    }

}
