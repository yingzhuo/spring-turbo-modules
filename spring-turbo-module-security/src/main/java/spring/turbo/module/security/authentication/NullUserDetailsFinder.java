package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    }

    /**
     * 获取单例的实例
     *
     * @return 实例
     */
    public static NullUserDetailsFinder getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        throw new UsernameNotFoundException(username);
    }

    @Nullable
    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {
        return null;
    }

    // 延迟加载
    private static class SyncAvoid {
        private static final NullUserDetailsFinder INSTANCE = new NullUserDetailsFinder();
    }

}
