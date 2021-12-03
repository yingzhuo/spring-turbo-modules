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
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import spring.turbo.bean.Attributes;
import spring.turbo.util.crypto.Keys;
import spring.turbo.util.crypto.PasswordAndSalt;

import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class UserDetailsPlusBuilder {

    private static final String DEFAULT_PASSWORD = "<NO PASSWORD>";

    private final User.UserBuilder userBuilder = User.builder();
    private final Attributes attributes = Attributes.newInstance();
    private Object id;
    private String nickname;
    private Object gender;
    private Object avatar;
    private Object nativeUser;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String biography;
    private PasswordAndSalt cryptoPasswordAndSalt;
    private Keys cryptoKeyPair;
    private boolean pwdFlag = false;

    UserDetailsPlusBuilder() {
        super();
    }

    public UserDetailsPlusBuilder username(String username) {
        userBuilder.username(username);
        return this;
    }

    public UserDetailsPlusBuilder password(String password) {
        pwdFlag = true;
        userBuilder.password(password);
        return this;
    }

    public UserDetailsPlusBuilder passwordEncoder(Function<String, String> encoder) {
        userBuilder.passwordEncoder(encoder);
        return this;
    }

    public UserDetailsPlusBuilder roles(String... roles) {
        userBuilder.roles(roles);
        return this;
    }

    public UserDetailsPlusBuilder authorities(GrantedAuthority... authorities) {
        userBuilder.authorities(authorities);
        return this;
    }

    public UserDetailsPlusBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
        userBuilder.authorities(authorities);
        return this;
    }

    public UserDetailsPlusBuilder authorities(String... authorities) {
        userBuilder.authorities(authorities);
        return this;
    }

    public UserDetailsPlusBuilder authoritiesWithCommaSeparatedString(String string) {
        return authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(string));
    }

    public UserDetailsPlusBuilder accountExpired(boolean accountExpired) {
        userBuilder.accountExpired(accountExpired);
        return this;
    }

    public UserDetailsPlusBuilder accountLocked(boolean accountLocked) {
        userBuilder.accountLocked(accountLocked);
        return this;
    }

    public UserDetailsPlusBuilder credentialsExpired(boolean credentialsExpired) {
        userBuilder.credentialsExpired(credentialsExpired);
        return this;
    }

    public UserDetailsPlusBuilder disabled(boolean disabled) {
        userBuilder.disabled(disabled);
        return this;
    }

    public UserDetailsPlusBuilder id(Object id) {
        this.id = id;
        return this;
    }

    public UserDetailsPlusBuilder nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserDetailsPlusBuilder gender(Object gender) {
        this.gender = gender;
        return this;
    }

    public UserDetailsPlusBuilder avatar(Object avatar) {
        this.avatar = avatar;
        return this;
    }

    public UserDetailsPlusBuilder nativeUser(Object u) {
        this.nativeUser = u;
        return this;
    }

    public UserDetailsPlusBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserDetailsPlusBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserDetailsPlusBuilder dateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserDetailsPlusBuilder biography(String bioInfo) {
        this.biography = bioInfo;
        return this;
    }

    public UserDetailsPlusBuilder cryptoPasswordAndSalt(PasswordAndSalt passwordAndSalt) {
        this.cryptoPasswordAndSalt = passwordAndSalt;
        return this;
    }

    public UserDetailsPlusBuilder cryptoKeyPair(Keys cryptoKeyPair) {
        this.cryptoKeyPair = cryptoKeyPair;
        return this;
    }

    public UserDetailsPlusBuilder putAttribute(String key, Object value) {
        this.attributes.add(key, value);
        return this;
    }

    public UserDetailsPlus build() {
        if (!pwdFlag) {
            userBuilder.password(DEFAULT_PASSWORD);
        }

        return new UserDetailsPlusImpl(userBuilder.build(),
                this.id,
                this.nickname,
                this.gender,
                this.avatar,
                this.nativeUser,
                this.email,
                this.phoneNumber,
                this.dateOfBirth,
                this.biography,
                this.cryptoPasswordAndSalt,
                this.cryptoKeyPair,
                this.attributes
        );
    }

}
