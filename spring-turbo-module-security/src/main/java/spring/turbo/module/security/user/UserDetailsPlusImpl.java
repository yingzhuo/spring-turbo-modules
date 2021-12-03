/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.bean.Attributes;
import spring.turbo.util.crypto.Keys;
import spring.turbo.util.crypto.PasswordAndSalt;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
class UserDetailsPlusImpl implements UserDetailsPlus {

    private final UserDetails delegate;
    private final Object id;
    private final String nickname;
    private final Object gender;
    private final Object avatar;
    private final Object nativeUser;
    private final String email;
    private final String phoneNumber;
    private final Date dateOfBirth;
    private final String bioInfo;
    private final PasswordAndSalt cryptoPasswordAndSalt;
    private final Keys cryptoKeyPair;
    private final Attributes attributes;

    UserDetailsPlusImpl(UserDetails delegate,
                        Object id,
                        String nickname,
                        Object gender,
                        Object avatar,
                        Object nativeUser,
                        String email,
                        String phoneNumber,
                        Date dateOfBirth,
                        String bioInfo,
                        PasswordAndSalt cryptoPasswordAndSalt,
                        Keys cryptoKeyPair,
                        Attributes attributes) {
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
        this.cryptoPasswordAndSalt = cryptoPasswordAndSalt;
        this.cryptoKeyPair = cryptoKeyPair;
        this.attributes = Optional.ofNullable(attributes).orElse(Attributes.newInstance());
    }

    @Override
    public <T> T getId() {
        return (T) id;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public <T> T getGender() {
        return (T) gender;
    }

    @Override
    public <T> T getAvatar() {
        return (T) this.avatar;
    }

    @Override
    public <T> T getNativeUser() {
        return (T) nativeUser;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String getBiography() {
        return bioInfo;
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

    @Override
    public PasswordAndSalt getCryptoPasswordAndSalt() {
        return cryptoPasswordAndSalt;
    }

    @Override
    public <T extends Keys> T getCryptoKeyPair() {
        return (T) cryptoKeyPair;
    }

}
