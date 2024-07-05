package spring.turbo.module.security.token.blacklist;

import spring.turbo.module.security.exception.BlacklistTokenException;
import spring.turbo.module.security.token.Token;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 2.0.5
 */
public final class NullTokenBlacklistManager implements TokenBlacklistManager {

    /**
     * 私有构造方法
     */
    private NullTokenBlacklistManager() {
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static NullTokenBlacklistManager getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public void save(Token token) {
        // 无动作
    }

    @Override
    public void verify(Token token) throws BlacklistTokenException {
        // 无动作
    }

    // 延迟加载
    private static final class SyncAvoid {
        private static final NullTokenBlacklistManager INSTANCE = new NullTokenBlacklistManager();
    }

}
