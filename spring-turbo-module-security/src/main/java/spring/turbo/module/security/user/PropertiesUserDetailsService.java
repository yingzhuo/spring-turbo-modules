package spring.turbo.module.security.user;

import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * {@link UserDetailsService} 简单实现。 用户一旦生成则不能修改。
 * <p>
 * 格式如下:
 * <pre>
 * bob={noop}bob,enabled,ROLE_USER,ROLE_ADMIN
 * </pre>
 *
 * @author 应卓
 * @see PropertiesUserDetailsServiceFactoryBean
 * @since 3.3.1
 */
public class PropertiesUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> delegate = new HashMap<>();

    public PropertiesUserDetailsService(@Nullable Properties users) {
        if (users == null) {
            return;
        }

        var usernames = users.propertyNames();
        var editor = new UserAttributeEditor();
        while (usernames.hasMoreElements()) {
            var username = (String) usernames.nextElement();
            editor.setAsText(users.getProperty(username));

            UserAttribute attr = (UserAttribute) editor.getValue();
            Assert.notNull(attr, () -> "The entry with username '" + username + "' could not be converted to an UserDetails");
            var user = createUserDetails(username, attr);
            this.delegate.put(username, user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.delegate.get(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
    }

    private User createUserDetails(String name, UserAttribute attr) {
        return new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
    }

}
