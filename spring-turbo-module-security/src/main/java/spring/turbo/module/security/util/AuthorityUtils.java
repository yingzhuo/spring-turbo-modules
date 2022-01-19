/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.util;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static spring.turbo.util.StringPool.COMMA;

/**
 * {@link org.springframework.security.core.GrantedAuthority} 相关工具
 *
 * @author 应卓
 * @see org.springframework.security.core.authority.AuthorityUtils
 * @since 1.0.5
 */
public final class AuthorityUtils {

    private static final List<GrantedAuthority> NO_AUTHORITIES = Collections.emptyList();

    /**
     * 私有构造方法
     */
    private AuthorityUtils() {
        super();
    }

    public static List<GrantedAuthority> noAuthorities() {
        return NO_AUTHORITIES;
    }

    public static List<GrantedAuthority> getAuthorities(@Nullable UserDetails userDetails) {
        if (userDetails == null) {
            return NO_AUTHORITIES;
        }
        final Collection<? extends GrantedAuthority> list = userDetails.getAuthorities();
        if (CollectionUtils.isEmpty(list)) {
            return NO_AUTHORITIES;
        } else {
            final List<GrantedAuthority> authorities = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
            return Collections.unmodifiableList(authorities);
        }
    }

    public static List<GrantedAuthority> createAuthorityList(@Nullable String... authorities) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (authorities != null) {
            for (String authority : authorities) {
                if (authority != null) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                }
            }
        }
        return Collections.unmodifiableList(grantedAuthorities);
    }

    public static List<GrantedAuthority> commaSeparatedStringToAuthorityList(@Nullable String authorityString) {
        if (StringUtils.isBlank(authorityString)) {
            return NO_AUTHORITIES;
        }

        final String[] authoritiesArray = authorityString.split(COMMA);
        for (int i = 0; i < authoritiesArray.length; i++) {
            String authority = authoritiesArray[i];
            if (authority != null) {
                authoritiesArray[i] = authority.trim();
            }
        }
        return createAuthorityList(authoritiesArray);
    }

}
