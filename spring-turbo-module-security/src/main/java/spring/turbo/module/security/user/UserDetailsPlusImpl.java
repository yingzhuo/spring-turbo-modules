package spring.turbo.module.security.user;

import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.util.collection.Attributes;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class UserDetailsPlusImpl implements UserDetailsPlus {

    @NonNull
    private final UserDetails delegate;

    @Nullable
    private final Object id;

    @Nullable
    private final Object avatar;

    @Nullable
    private final Object nativeUser;

    @Nullable
    private final String email;

    @Nullable
    private final String phoneNumber;

    @Nullable
    private final LocalDate dateOfBirth;

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

    UserDetailsPlusImpl(UserDetails delegate, @Nullable Object id, @Nullable Object avatar,
                        @Nullable Object nativeUser, @Nullable String email, @Nullable String phoneNumber,
                        @Nullable LocalDate dateOfBirth, @Nullable String bioInfo, @Nullable String nationality,
                        @Nullable String location, @Nullable String url, @Nullable Attributes attributes) {
        this.delegate = Objects.requireNonNull(delegate);
        this.id = id;
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
    public LocalDate getDateOfBirth() {
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

    @Override
    public String toString() {
        var creator = new ToStringCreator(this);
        creator.append("username", getUsername());
        creator.append("password", "[PROTECTED]");
        return creator.toString();
    }

}
