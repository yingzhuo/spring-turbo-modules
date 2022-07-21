/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.user;

import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.bean.Attributes;
import spring.turbo.util.Asserts;

import java.util.Date;
import java.util.Optional;

/**
 * 增强型 {@link UserDetails}
 * <p>
 * 为 {@link UserDetails} 增加若干方法使之更方便
 *
 * @author 应卓
 * @see UserDetails
 * @see UserDetailsPlusBuilder
 * @see #builder()
 * @see UserDetailsPlusImpl
 * @see spring.turbo.module.security.authentication.TokenToUserConverter
 * @since 1.0.0
 */
public interface UserDetailsPlus extends UserDetails {

    /**
     * 获取创建器
     *
     * @return 创建器实例
     */
    public static UserDetailsPlusBuilder builder() {
        return new UserDetailsPlusBuilder();
    }

    @Nullable
    public <T> T getId();

    public default <T> T getRequiredId() {
        T id = getId();
        Asserts.notNull(id);
        return id;
    }

    @Nullable
    public String getNickname();

    public default String getRequiredNickname() {
        String nickname = getNickname();
        Asserts.notNull(nickname);
        return nickname;
    }

    /**
     * 性别
     *
     * @param <T> 表达性别的类型
     * @return 性别
     * @see spring.turbo.bean.Gender
     */
    @Nullable
    public <T> T getGender();

    public default <T> T getRequiredGender() {
        T gender = getGender();
        Asserts.notNull(gender);
        return gender;
    }

    @Nullable
    public <T> T getAvatar();

    public default <T> T getRequiredAvatar() {
        T avatar = getAvatar();
        Asserts.notNull(avatar);
        return avatar;
    }

    @Nullable
    public <T> T getNativeUser();

    public default <T> T getRequiredNativeUser() {
        T user = getNativeUser();
        Asserts.notNull(user);
        return user;
    }

    @Nullable
    public String getEmail();

    public default String getRequiredEmail() {
        String email = getEmail();
        Asserts.notNull(email);
        return email;
    }

    @Nullable
    public String getPhoneNumber();

    public default String getRequiredPhoneNumber() {
        String phoneNumber = getPhoneNumber();
        Asserts.notNull(phoneNumber);
        return phoneNumber;
    }

    @Nullable
    public Date getDateOfBirth();

    public default Date getRequiredDateOfBirth() {
        Date dob = getDateOfBirth();
        Asserts.notNull(dob);
        return dob;
    }

    @Nullable
    public String getBiography();

    public default String getRequiredBiography() {
        String bio = getBiography();
        Asserts.notNull(bio);
        return bio;
    }

    @Nullable
    public Attributes getAttributes();

    public default Attributes getRequiredAttributes() {
        return Optional.ofNullable(getAttributes()).orElseGet(Attributes::newInstance);
    }

}
