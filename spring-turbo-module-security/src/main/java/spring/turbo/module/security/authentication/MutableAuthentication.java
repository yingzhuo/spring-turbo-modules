/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @since 3.1.1
 */
public interface MutableAuthentication extends Authentication {

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities();

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities);

    @Nullable
    @Override
    public Object getCredentials();

    public void setCredentials(Object credentials);

    @Nullable
    @Override
    public Object getDetails();

    public void setDetails(Object details);

    @Nullable
    @Override
    public Object getPrincipal();

    public void setPrincipal(Object principal);

    @Override
    public boolean isAuthenticated();

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

    @Override
    public default String getName() {
        if (this.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) this.getPrincipal()).getUsername();
        }
        if (this.getPrincipal() instanceof AuthenticatedPrincipal) {
            return ((AuthenticatedPrincipal) this.getPrincipal()).getName();
        }
        if (this.getPrincipal() instanceof Principal) {
            return ((Principal) this.getPrincipal()).getName();
        }
        return (this.getPrincipal() == null) ? EMPTY : this.getPrincipal().toString();
    }

}
