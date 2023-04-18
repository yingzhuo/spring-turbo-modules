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
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.lang.Nullable;
import spring.turbo.util.StringUtils;
import spring.turbo.util.crypto.Base64;

/**
 * 国密算法，由Hutool工具包提供
 *
 * @author 应卓
 * @since 2.2.2
 */
public final class SM2Algorithm extends AbstractAlgorithm {

    private static final String ALGORITHM_NAME = "SM2";

    private final SM2 sign;

    @Nullable
    private final String withId;

    public SM2Algorithm(String publicKey, String privateKey) {
        this(publicKey, privateKey, null);
    }

    public SM2Algorithm(String publicKey, String privateKey, @Nullable String withId) {
        super(ALGORITHM_NAME, ALGORITHM_NAME);
        this.sign = createSign(publicKey, privateKey);
        this.withId = withId;
    }

    private SM2 createSign(String publicKey, String privateKey) {
        publicKey = StringUtils.deleteWhitespace(publicKey); // 主要是为了去除换行
        privateKey = StringUtils.deleteWhitespace(privateKey); // 主要是为了去除换行
        return new SM2(privateKey, publicKey);
    }

    @Override
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        try {
            return sign.sign(contentBytes, getWithIdBytes());
        } catch (Throwable e) {
            throw new SignatureGenerationException(this, e);
        }
    }

    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        var success = false;

        try {
            var signatureBytes = Base64.toBytes(jwt.getSignature());
            var dataBytes = combineHeaderAndPayload(jwt);
            success = sign.verify(dataBytes, signatureBytes, getWithIdBytes());
        } catch (Throwable e) {
            throw new SignatureVerificationException(this, e);
        }

        if (!success) {
            throw new SignatureVerificationException(this);
        }
    }

    @Nullable
    private byte[] getWithIdBytes() {
        if (this.withId != null) {
            return this.withId.getBytes();
        } else {
            return null;
        }
    }

}
