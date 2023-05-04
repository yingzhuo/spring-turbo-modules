/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.token.blacklist;

import spring.turbo.module.security.exception.BlacklistTokenException;
import spring.turbo.module.security.token.Token;

/**
 * @author 应卓
 *
 * @since 2.0.6
 */
public final class AlwaysBlacklistedTokenBlacklistedManager implements TokenBlacklistManager {

    /**
     * 私有构造方法
     */
    private AlwaysBlacklistedTokenBlacklistedManager() {
        super();
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
