/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.misc;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.SignerUtils;

import java.security.KeyPair;
import java.util.Base64;

/**
 * 通用非对称加密算法签名器
 *
 * @author 应卓
 * @since 3.3.1
 */
public class JavaJwtAsymmetricAlgorithmFactoryBean implements FactoryBean<Algorithm>, InitializingBean {

    private String sigAlgName;
    private KeyPair keyPair;

    /**
     * 默认构造方法
     */
    public JavaJwtAsymmetricAlgorithmFactoryBean() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Algorithm getObject() {
        return new AsymmetricAlgorithm(sigAlgName, keyPair);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return Algorithm.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        Asserts.hasText(sigAlgName, "sigAlgName is required");
        Asserts.notNull(keyPair, "keyPair is required");
    }

    public void setSigAlgName(String sigAlgName) {
        this.sigAlgName = sigAlgName;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class AsymmetricAlgorithm extends Algorithm {

        private final KeyPair keyPair;
        private final String sigAlgName;

        public AsymmetricAlgorithm(String sigAlgName, KeyPair keyPair) {
            super(sigAlgName, sigAlgName);
            this.keyPair = keyPair;
            this.sigAlgName = sigAlgName;
        }

        @Override
        public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
            return SignerUtils.sign(keyPair.getPrivate(), contentBytes, sigAlgName);
        }

        @Override
        public void verify(DecodedJWT jwt) throws SignatureVerificationException {
            byte[] headerBytes = Base64.getUrlDecoder().decode(jwt.getHeader());
            byte[] payloadBytes = Base64.getUrlDecoder().decode(jwt.getPayload());
            byte[] contentBytes = new byte[headerBytes.length + 1 + payloadBytes.length];

            System.arraycopy(headerBytes, 0, contentBytes, 0, headerBytes.length);
            contentBytes[headerBytes.length] = (byte) '.';
            System.arraycopy(payloadBytes, 0, contentBytes, headerBytes.length + 1, payloadBytes.length);

            byte[] signatureBytes = Base64.getUrlDecoder().decode(jwt.getSignature());
            SignerUtils.verify(
                    keyPair.getPublic(),
                    contentBytes,
                    signatureBytes,
                    sigAlgName
            );
        }
    }

}
