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
 * @see #getInstance()
 * @since 2.0.5
 */
public final class NullTokenBlacklistManager implements TokenBlacklistManager {

    public static NullTokenBlacklistManager getInstance() {
        return SyncAvoid.INSTANCE;
    }

    /**
     * 私有构造方法
     */
    private NullTokenBlacklistManager() {
        super();
    }

    @Override
    public void verify(Token token) throws BlacklistTokenException {
        // nop
    }

    // 延迟加载
    private static final class SyncAvoid {
        private static final NullTokenBlacklistManager INSTANCE = new NullTokenBlacklistManager();
    }

}
