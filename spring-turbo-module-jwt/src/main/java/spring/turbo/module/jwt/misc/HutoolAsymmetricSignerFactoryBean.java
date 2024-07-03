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
import spring.turbo.util.Asserts;

import java.security.KeyPair;

/**
 * 通用非对称加密算法签名器
 *
 * @author 应卓
 * @since 3.3.1
 */
public class HutoolAsymmetricSignerFactoryBean implements FactoryBean<JWTSigner>, InitializingBean {

    private String sigAlgName;
    private KeyPair keyPair;

    /**
     * 默认构造方法
     */
    public HutoolAsymmetricSignerFactoryBean() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JWTSigner getObject() {
        return JWTSignerUtil.createSigner(sigAlgName, keyPair);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return JWTSigner.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        Asserts.hasText(sigAlgName, "sigAlgName is required");
        Asserts.notNull(keyPair, "keyPair is required");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setSigAlgName(String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

}
