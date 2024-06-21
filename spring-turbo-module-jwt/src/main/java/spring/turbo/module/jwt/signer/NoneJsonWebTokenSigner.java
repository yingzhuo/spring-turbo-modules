/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.signer;

import spring.turbo.util.StringPool;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 3.3.1
 */
public final class NoneJsonWebTokenSigner implements JsonWebTokenSigner {

    /**
     * 私有构造方法
     */
    private NoneJsonWebTokenSigner() {
        super();
    }

    public static NoneJsonWebTokenSigner getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public String sign(String headerBase64, String payloadBase64) {
        return StringPool.EMPTY;
    }

    @Override
    public boolean verify(String headerBase64, String payloadBase64, String signBase64) {
        return StringPool.EMPTY.equals(signBase64);
    }

    @Override
    public String getAlgorithm() {
        return "none";
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    private static class SyncAvoid {
        private static final NoneJsonWebTokenSigner INSTANCE = new NoneJsonWebTokenSigner();
    }

}
