/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.util.function.Supplier;

/**
 * JWT令牌工厂实现
 *
 * @author 应卓
 *
 * @since 2.2.4
 */
public final class JwtTokenFactoryImpl implements JwtTokenFactory {

    private final JWTSigner signer;
    private boolean overrideType = true;
    private boolean overrideAlgorithm = true;
    private boolean overrideIssueAt = true;
    private @Nullable Supplier<String> keyIdSupplier = null;

    /**
     * 构造方法
     *
     * @param signer
     *            签名算法实例
     */
    public JwtTokenFactoryImpl(JWTSigner signer) {
        Asserts.notNull(signer, "signer is null");
        this.signer = signer;
    }

    /**
     * {@inheritDoc}
     *
     * @param data
     *            令牌头部与负载的数据
     *
     * @return
     */
    @Override
    public String create(Data data) {

        // 强行覆盖 header - typ
        if (overrideType) {
            data.type("JWT");
        }

        // 强行覆盖 header - alg
        if (overrideAlgorithm) {
            data.algorithm(signer.getAlgorithmId());
        }

        // 强行覆盖 header - keyId
        if (keyIdSupplier != null) {
            var keyId = keyIdSupplier.get();
            if (keyId != null) {
                data.keyId(keyId);
            }
        }

        // 强行覆盖 payload - iat
        if (overrideIssueAt) {
            data.issuedAtNow();
        }

        // 生成令牌
        return JWTUtil.createToken(data.getHeaderMap(), data.getPayloadMap(), signer);
    }

    public JWTSigner getSigner() {
        return signer;
    }

    public void setOverrideType(boolean overrideType) {
        this.overrideType = overrideType;
    }

    public void setOverrideAlgorithm(boolean overrideAlgorithm) {
        this.overrideAlgorithm = overrideAlgorithm;
    }

    public void setOverrideIssueAt(boolean overrideIssueAt) {
        this.overrideIssueAt = overrideIssueAt;
    }

    public void setKeyIdSupplier(@Nullable Supplier<String> keyIdSupplier) {
        this.keyIdSupplier = keyIdSupplier;
    }

}
