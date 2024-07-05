package spring.turbo.module.security.authentication;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 通过用户名和密码查找{@link UserDetails}实例的部件
 *
 * @author 应卓
 * @see TokenToUserConverter
 * @see spring.turbo.module.security.filter.BasicAuthenticationFilter
 * @see UserDetails
 * @see spring.turbo.module.security.user.UserDetailsPlus
 * @since 1.2.3
 */
public interface UserDetailsFinder {

    @NonNull
    public UserDetails loadUserByUsername(String username) throws AuthenticationException;

    @Nullable
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException;

}
