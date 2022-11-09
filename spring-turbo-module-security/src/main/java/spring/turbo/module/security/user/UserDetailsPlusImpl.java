/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.user;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.bean.Attributes;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class UserDetailsPlusImpl implements UserDetailsPlus, Serializable {

    @NonNull
    private final UserDetails delegate;

    @Nullable
    private final Object id;

    @Nullable
    private final String nickname;

    @Nullable
    private final Object gender;

    @Nullable
    private final Object avatar;

    @Nullable
    private final Object nativeUser;

    @Nullable
    private final String email;

    @Nullable
    private final String phoneNumber;

    @Nullable
    private final Date dateOfBirth;

    @Nullable
    private final String bioInfo;

    @Nullable
    private final String nationality;

    @Nullable
    private final String location;

    @Nullable
    private final String url;

    @NonNull
    private final Attributes attributes;

    UserDetailsPlusImpl(UserDetails delegate,
                        @Nullable Object id,
                        @Nullable String nickname,
                        @Nullable Object gender,
                        @Nullable Object avatar,
                        @Nullable Object nativeUser,
                        @Nullable String email,
                        @Nullable String phoneNumber,
                        @Nullable Date dateOfBirth,
                        @Nullable String bioInfo,
                        @Nullable String nationality,
                        @Nullable String location,
                        @Nullable String url,
                        @Nullable Attributes attributes) {
        this.delegate = Objects.requireNonNull(delegate);
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.avatar = avatar;
        this.nativeUser = nativeUser;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.bioInfo = bioInfo;
        this.nationality = nationality;
        this.location = location;
        this.url = url;
        this.attributes = Optional.ofNullable(attributes).orElseGet(Attributes::newInstance);
    }

    @Override
    @Nullable
    public <T> T getId() {
        return (T) id;
    }

    @Override
    @Nullable
    public String getNickname() {
        return nickname;
    }

    @Override
    @Nullable
    public <T> T getGender() {
        return (T) gender;
    }

    @Override
    @Nullable
    public <T> T getAvatar() {
        return (T) this.avatar;
    }

    @Override
    @Nullable
    public <T> T getNativeUser() {
        return (T) nativeUser;
    }

    @Override
    @Nullable
    public String getEmail() {
        return email;
    }

    @Override
    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    @Nullable
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    @Nullable
    public String getBiography() {
        return bioInfo;
    }

    @Override
    @Nullable
    public String getNationality() {
        return nationality;
    }

    @Override
    @Nullable
    public String getLocation() {
        return location;
    }

    @Override
    @Nullable
    public String getUrl() {
        return url;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return delegate.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

}
