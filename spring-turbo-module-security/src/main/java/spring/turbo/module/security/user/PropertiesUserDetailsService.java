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
 * root={bcrypt}$2a$10$zs3L/xHDTjxZw6KO/n/q1e4WV27Lh8o/NzBTytwSK14xY5NGrAkwm,enabled,ROLE_USER,ROLE_ADMIN
 * </pre>
 *
 * @author 应卓
 * @see Properties
 * @see UserDetails
 * @since 3.3.1
 */
public class PropertiesUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> map = new HashMap<>();

    /**
     * 默认构造方法
     */
    public PropertiesUserDetailsService() {
    }

    /**
     * 构造方法
     *
     * @param users 用户数据
     */
    public PropertiesUserDetailsService(@Nullable Properties users) {
        loadData(users);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = map.get(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
    }

    public void addProperties(@Nullable Properties properties) {
        loadData(properties);
    }

    public void addProperties(@Nullable Map<?, ?> properties) {
        loadData(properties);
    }

    private void loadData(@Nullable Map<?, ?> users) {
        if (users != null) {
            var editor = new UserAttributeEditor();
            for (Object key : users.keySet()) {
                Object value = users.get(key);

                var username = key.toString();
                editor.setAsText(value.toString());
                var attr = (UserAttribute) editor.getValue();
                Assert.notNull(attr, () -> "The entry with username '" + username + "' could not be converted to an UserDetails");
                var user = createUserDetails(username, attr);
                map.put(username, user);
            }
        }
    }

    private User createUserDetails(String name, UserAttribute attr) {
        return new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
    }

    /**
     * 获取用户的数量
     *
     * @return 用户的数量
     * @see Map#size()
     */
    public int size() {
        return map.size();
    }

    /**
     * 判断包含的用户量是否为空
     *
     * @return 为空时返回 {@code true}
     * @see Map#isEmpty()
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

}
