/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.algorithm;

import cn.hutool.crypto.asymmetric.SM2;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.Base64;

/**
 * 国密算法 (SM2)
 *
 * @author 应卓
 * @since 2.2.2
 */
public class SM2AlgorithmFactoryBean implements FactoryBean<Algorithm> {

    private String privateKey;
    private String publicKey;
    private String withId;

    /**
     * 默认构造方法
     */
    public SM2AlgorithmFactoryBean() {
        super();
    }

    @Override
    public Algorithm getObject() {
        Asserts.notNull(privateKey);
        Asserts.notNull(publicKey);
        return new SM2Algorithm(publicKey, privateKey, withId);
    }

    @Override
    public Class<?> getObjectType() {
        return Algorithm.class;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Nullable
    public String getWithId() {
        return withId;
    }

    public void setWithId(String withId) {
        this.withId = withId;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class SM2Algorithm extends AbstractAlgorithm {

        private final SM2 sm2;
        private String withId = "1234567812345678";

        public SM2Algorithm(String publicKey, String privateKey, @Nullable String withId) {
            super("SM2", "SM2国密算法");
            this.sm2 = new SM2(privateKey, publicKey);
            if (withId != null) {
                this.withId = withId;
            }
        }

        @Override
        public void verify(DecodedJWT jwt) throws SignatureVerificationException {
            try {
                final byte[] signatureBytes = Base64.toBytes(jwt.getSignature());
                final byte[] dataBytes = super.combineHeaderAndPayload(jwt);
                boolean success = sm2.verify(dataBytes, signatureBytes, withId.getBytes());
                if (!success) {
                    throw new SignatureVerificationException(this);
                }
            } catch (Throwable e) {
                throw new SignatureVerificationException(this);
            }
        }

        @Override
        public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
            try {
                return sm2.sign(contentBytes, withId.getBytes());
            } catch (Throwable e) {
                throw new SignatureGenerationException(this, e);
            }
        }
    }

}
