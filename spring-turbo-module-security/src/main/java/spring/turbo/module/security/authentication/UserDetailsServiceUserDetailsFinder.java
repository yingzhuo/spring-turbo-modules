package spring.turbo.module.security.authentication;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.util.Asserts;

import java.util.Objects;

/**
 * @author 应卓
 * @since 1.3.1
 */
@SuppressWarnings("deprecation")
public class UserDetailsServiceUserDetailsFinder implements UserDetailsFinder {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceUserDetailsFinder(UserDetailsService userDetailsService) {
        this(userDetailsService, NoOpPasswordEncoder.getInstance());
    }

    public UserDetailsServiceUserDetailsFinder(UserDetailsService userDetailsService,
                                               @Nullable PasswordEncoder passwordEncoder) {
        Asserts.notNull(userDetailsService);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = Objects.requireNonNullElseGet(passwordEncoder, NoOpPasswordEncoder::getInstance);
    }

    @NonNull
    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }

    @Nullable
    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password) throws AuthenticationException {
        UserDetails ud = userDetailsService.loadUserByUsername(username);
        if (ud == null || ud.getPassword() == null) {
            return null;
        }
        return passwordEncoder.matches(password, ud.getPassword()) ? ud : null;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

}
