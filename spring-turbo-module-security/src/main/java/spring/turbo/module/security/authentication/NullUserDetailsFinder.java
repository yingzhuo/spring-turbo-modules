/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author 应卓
 * @see #getInstance()
 * @see UserDetails
 * @see spring.turbo.module.security.user.UserDetailsPlus
 * @since 1.2.3
 */
public final class NullUserDetailsFinder implements UserDetailsFinder {

    /**
     * 私有构造方法
     */
    private NullUserDetailsFinder() {
        super();
    }

    public static NullUserDetailsFinder getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Nullable
    @Override
    public UserDetails find(String username, String password) throws AuthenticationException {
        return null;
    }

    private static class SyncAvoid {
        private static final NullUserDetailsFinder INSTANCE = new NullUserDetailsFinder();
    }

}
