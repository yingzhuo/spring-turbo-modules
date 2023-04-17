/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt.algorithm;

import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.Base64;

/**
 * @author 应卓
 * @since 2.2.2
 */
@Getter
@Setter
public final class HutoolSignAlgorithmFactoryBean implements FactoryBean<Algorithm> {

    private SignAlgorithm signAlgorithm;
    private String publicKey;
    private String privateKey;

    @Override
    public Algorithm getObject() {
        Asserts.notNull(signAlgorithm, "signAlgorithm not set");
        Asserts.hasText(publicKey, "publicKey not set");
        Asserts.hasText(privateKey, "privateKey not set");
        return new HutoolSignAlgorithm(signAlgorithm, publicKey, privateKey);
    }

    @Override
    public Class<?> getObjectType() {
        return Algorithm.class;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class HutoolSignAlgorithm extends AbstractAlgorithm {

        private final Sign sign;

        public HutoolSignAlgorithm(SignAlgorithm signAlgorithm, String publicKey, String privateKey) {
            super(signAlgorithm.name(), signAlgorithm.name());
            this.sign = SignUtil.sign(signAlgorithm, privateKey, publicKey);
        }

        @Override
        public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
            try {
                return sign.sign(contentBytes);
            } catch (Exception e) {
                throw new SignatureGenerationException(this, e);
            }
        }

        @Override
        public void verify(DecodedJWT jwt) throws SignatureVerificationException {
            try {
                final byte[] signatureBytes = Base64.toBytes(jwt.getSignature());
                final byte[] dataBytes = super.combineHeaderAndPayload(jwt);
                boolean success = sign.verify(dataBytes, signatureBytes);
                if (!success) {
                    throw new SignatureVerificationException(this);
                }
            } catch (SignatureVerificationException e) {
                throw new SignatureVerificationException(this);
            }
        }
    }

}
