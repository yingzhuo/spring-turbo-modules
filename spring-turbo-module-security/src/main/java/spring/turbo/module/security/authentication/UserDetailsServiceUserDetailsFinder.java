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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.module.security.encoder.NullPasswordEncoder;
import spring.turbo.util.Asserts;

import java.util.Objects;

/**
 * @author 应卓
 * @since 1.3.1
 */
public class UserDetailsServiceUserDetailsFinder implements UserDetailsFinder {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceUserDetailsFinder(UserDetailsService userDetailsService) {
        this(userDetailsService, null);
    }

    public UserDetailsServiceUserDetailsFinder(UserDetailsService userDetailsService, @Nullable PasswordEncoder passwordEncoder) {
        Asserts.notNull(userDetailsService);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder != null ? passwordEncoder : NullPasswordEncoder.getInstance();
    }

    @Nullable
    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {
        UserDetails ud = userDetailsService.loadUserByUsername(username);
        if (ud == null) {
            return null;
        }
        return Objects.equals(password, ud.getPassword()) ? ud : null;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

}
