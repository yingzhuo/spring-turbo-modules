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
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import spring.turbo.util.StringUtils;
import spring.turbo.util.crypto.Base64;

/**
 * Hutool工具包提供的签名算法用于JWT签名
 *
 * @author 应卓
 * @since 2.2.2
 */
public final class HutoolSignAlgorithm extends AbstractAlgorithm {

    private final Sign sign;

    public HutoolSignAlgorithm(SignAlgorithm signAlgorithm, String publicKey, String privateKey) {
        super(signAlgorithm.name(), signAlgorithm.name());
        this.sign = createSign(signAlgorithm, publicKey, privateKey);
    }

    private Sign createSign(SignAlgorithm signAlgorithm, String publicKey, String privateKey) {
        publicKey = StringUtils.deleteWhitespace(publicKey); // 主要是为了去除换行
        privateKey = StringUtils.deleteWhitespace(privateKey); // 主要是为了去除换行
        return SignUtil.sign(signAlgorithm, privateKey, publicKey);
    }

    @Override
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        try {
            return sign.sign(contentBytes);
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
            success = sign.verify(dataBytes, signatureBytes);
        } catch (Throwable e) {
            throw new SignatureVerificationException(this, e);
        }

        if (!success) {
            throw new SignatureVerificationException(this);
        }
    }
}
