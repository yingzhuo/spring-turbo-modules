/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.misc;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.lang.Nullable;

/**
 * @author 应卓
 * @since 3.3.1
 */
public final class HutoolSM2JWTSinger implements JWTSigner {

    private final SM2 sm2;

    @Nullable
    private final byte[] withId;

    public HutoolSM2JWTSinger(SM2 sm2, @Nullable String withId) {
        this.sm2 = sm2;
        this.withId = withId != null ? withId.getBytes() : null;
    }

    @Override
    public String sign(String headerBase64, String payloadBase64) {
        var data = StrUtil.format("{}.{}", headerBase64, payloadBase64).getBytes();
        return Base64.encodeUrlSafe(sm2.sign(data, withId));
    }

    @Override
    public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
        return sm2.verify(StrUtil.bytes(StrUtil.format("{}.{}", headerBase64, payloadBase64)),
                Base64.decode(signBase64), withId);
    }

    @Override
    public String getAlgorithm() {
        return "SM2";
    }

    @Override
    public String getAlgorithmId() {
        return "SM2";
    }
}
