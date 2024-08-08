package spring.turbo.module.security.user;

import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
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
    private final UserDetails delegating;

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

    //@formatter:off
    UserDetailsPlusImpl(UserDetails delegating,
                        @Nullable Object id,
                        @Nullable Object avatar,
                        @Nullable Object nativeUser,
                        @Nullable String email,
                        @Nullable String phoneNumber,
                        @Nullable LocalDate dateOfBirth,
                        @Nullable String bioInfo,
                        @Nullable String nationality,
                        @Nullable String location,
                        @Nullable String url,
                        @Nullable Attributes attributes)
    {
        Assert.notNull(delegating, "delegating is required");

        this.delegating = Objects.requireNonNull(delegating);
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
    //@formatter:on

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T getId() {
        return (T) id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T getAvatar() {
        return (T) this.avatar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T getNativeUser() {
        return (T) nativeUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getEmail() {
        return email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getBiography() {
        return bioInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getNationality() {
        return nationality;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getLocation() {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegating.getAuthorities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return delegating.getPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return delegating.getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonExpired() {
        return delegating.isAccountNonExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAccountNonLocked() {
        return delegating.isAccountNonLocked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return delegating.isCredentialsNonExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return delegating.isEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        var creator = new ToStringCreator(this);
        creator.append("username", getUsername());
        creator.append("password", "[PROTECTED]");
        return creator.toString();
    }
}
