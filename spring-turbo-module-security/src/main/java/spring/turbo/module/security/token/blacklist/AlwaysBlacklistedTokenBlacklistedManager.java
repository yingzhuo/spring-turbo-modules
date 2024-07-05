package spring.turbo.module.security.token.blacklist;

import spring.turbo.module.security.exception.BlacklistTokenException;
import spring.turbo.module.security.token.Token;

/**
 * @author 应卓
 * @since 2.0.6
 */
public final class AlwaysBlacklistedTokenBlacklistedManager implements TokenBlacklistManager {

    /**
     * 私有构造方法
     */
    private AlwaysBlacklistedTokenBlacklistedManager() {
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static AlwaysBlacklistedTokenBlacklistedManager getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public void save(Token token) {
        // nop
    }

    @Override
    public void verify(Token token) throws BlacklistTokenException {
        throw new BlacklistTokenException("");
    }

    // 延迟加载
    private static final class SyncAvoid {
        private static final AlwaysBlacklistedTokenBlacklistedManager INSTANCE = new AlwaysBlacklistedTokenBlacklistedManager();
    }

}
