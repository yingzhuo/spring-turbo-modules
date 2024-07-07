package spring.turbo.module.security.user;

import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.util.Asserts;
import spring.turbo.util.collection.Attributes;

import java.io.Serializable;
import java.util.Date;

/**
 * 增强型 {@link UserDetails}
 * <p>
 * 为 {@link UserDetails} 增加若干方法使之更方便
 *
 * @author 应卓
 * @see UserDetails
 * @see #builder()
 * @see spring.turbo.module.security.authentication.TokenToUserConverter
 * @since 1.0.0
 */
public interface UserDetailsPlus extends UserDetails, Serializable {

    /**
     * 获取创建器
     *
     * @return 创建器实例
     */
    public static UserDetailsPlusBuilder builder() {
        return new UserDetailsPlusBuilder();
    }

    /**
     * 获取ID
     *
     * @param <T> ID类型
     * @return ID或{@code null}
     */
    @Nullable
    public <T> T getId();

    /**
     * 获取ID
     *
     * @param <T> ID类型
     * @return ID
     */
    public default <T> T getRequiredId() {
        T id = getId();
        Asserts.notNull(id);
        return id;
    }

    /**
     * 获取绰号
     *
     * @return 绰号或{@code null}
     */
    @Nullable
    public String getNickname();

    /**
     * 获取绰号
     *
     * @return 绰号
     */
    public default String getRequiredNickname() {
        String nickname = getNickname();
        Asserts.notNull(nickname);
        return nickname;
    }

    /**
     * 头像
     *
     * @param <T> 头像的类型
     * @return 头像或{@code null}
     */
    @Nullable
    public <T> T getAvatar();

    /**
     * 头像
     *
     * @param <T> 头像的类型
     * @return 头像
     */
    public default <T> T getRequiredAvatar() {
        T avatar = getAvatar();
        Asserts.notNull(avatar);
        return avatar;
    }

    /**
     * Email
     *
     * @return 电子邮件地址或{@code null}
     */
    @Nullable
    public String getEmail();

    /**
     * Email
     *
     * @return 电子邮件地址
     */
    public default String getRequiredEmail() {
        String email = getEmail();
        Asserts.notNull(email);
        return email;
    }

    /**
     * 电话号码
     *
     * @return 电话号码或{@code null}
     */
    @Nullable
    public String getPhoneNumber();

    /**
     * 电话号码
     *
     * @return 电话号码
     */
    public default String getRequiredPhoneNumber() {
        String phoneNumber = getPhoneNumber();
        Asserts.notNull(phoneNumber);
        return phoneNumber;
    }

    /**
     * 出生日期
     *
     * @return 出生日期或{@code null}
     */
    @Nullable
    public Date getDateOfBirth();

    /**
     * 出生日期
     *
     * @return 出生日期
     */
    public default Date getRequiredDateOfBirth() {
        Date dob = getDateOfBirth();
        Asserts.notNull(dob);
        return dob;
    }

    /**
     * BIO
     *
     * @return bio或{@code null}
     */
    @Nullable
    public String getBiography();

    /**
     * BIO
     *
     * @return bio
     */
    public default String getRequiredBiography() {
        String bio = getBiography();
        Asserts.notNull(bio);
        return bio;
    }

    /**
     * 国际
     *
     * @return 国际或{@code null}
     */
    public String getNationality();

    /**
     * 国际
     *
     * @return 国际
     */
    public default String getRequiredNationality() {
        String nationality = getNationality();
        Asserts.notNull(nationality);
        return nationality;
    }

    /**
     * 位置
     *
     * @return 位置或{@code null}
     */
    @Nullable
    public String getLocation();

    /**
     * 位置
     *
     * @return 位置
     */
    public default String getRequiredLocation() {
        String location = getLocation();
        Asserts.notNull(location);
        return location;
    }

    /**
     * URL
     *
     * @return URL或{@code null}
     */
    @Nullable
    public String getUrl();

    /**
     * URL
     *
     * @return URL
     */
    public default String getRequiredUrl() {
        String url = getUrl();
        Asserts.notNull(url);
        return url;
    }

    /**
     * 其他信息
     *
     * @return 其他信息
     */
    public Attributes getAttributes();

    /**
     * 用户对象
     *
     * @param <T> 用户对象类型
     * @return 用户对象或{@code null}
     */
    @Nullable
    public <T> T getNativeUser();

    /**
     * 用户对象
     *
     * @param <T> 用户对象类型
     * @return 用户对象
     */
    public default <T> T getRequiredNativeUser() {
        T user = getNativeUser();
        Asserts.notNull(user);
        return user;
    }

}
