/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.jwt;

import cn.hutool.crypto.asymmetric.SM2;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.lang.NonNull;
import spring.turbo.module.security.jwt.AbstractAlgorithm;
import spring.turbo.util.Asserts;
import spring.turbo.util.crypto.Base64;

/**
 * 国密算法 (SM2)
 *
 * @author 应卓
 * @since 1.0.2
 */
public final class SM2Algorithm extends AbstractAlgorithm {

    private final static String SM2 = "SM2";

    private final SM2 sm2;

    public SM2Algorithm(@NonNull String publicKey, @NonNull String privateKey) {
        super(SM2, SM2);
        Asserts.hasText(publicKey);
        Asserts.hasText(privateKey);
        this.sm2 = new SM2(Base64.toBytes(privateKey), Base64.toBytes(publicKey));
    }

    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        final byte[] signatureBytes = Base64.toBytes(jwt.getSignature());
        final byte[] dataBytes = super.combineHeaderAndPayload(jwt);
        try {
            boolean success = sm2.verify(dataBytes, signatureBytes);
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
            return sm2.sign(contentBytes);
        } catch (Throwable e) {
            throw new SignatureGenerationException(this, e);
        }
    }

}
