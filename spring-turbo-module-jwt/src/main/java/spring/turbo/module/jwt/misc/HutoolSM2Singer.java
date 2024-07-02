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

import java.util.Optional;

/**
 * SM2签名器 (国密算法)
 *
 * @author 应卓
 * @since 3.3.1
 */
public final class HutoolSM2Singer implements JWTSigner {

    private final SM2 sm2;

    @Nullable
    private final byte[] withId;

    /**
     * 构造方法
     *
     * @param sm2 sm2加密器
     */
    public HutoolSM2Singer(SM2 sm2) {
        this(sm2, null);
    }

    /**
     * 构造方法
     *
     * @param sm2    sm2加密器
     * @param withId ID
     */
    public HutoolSM2Singer(SM2 sm2, @Nullable String withId) {
        this.sm2 = sm2;
        this.withId = Optional.ofNullable(withId).map(String::getBytes).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sign(String headerBase64, String payloadBase64) {
        var data = StrUtil.format("{}.{}", headerBase64, payloadBase64).getBytes();
        return Base64.encodeUrlSafe(sm2.sign(data, withId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
        return sm2.verify(
                StrUtil.bytes(StrUtil.format("{}.{}", headerBase64, payloadBase64)),
                Base64.decode(signBase64), withId
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlgorithm() {
        return "SM2";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlgorithmId() {
        return "SM2";
    }

}
