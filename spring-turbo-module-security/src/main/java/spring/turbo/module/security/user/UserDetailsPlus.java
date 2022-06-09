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

import java.util.Date;

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

    @Nullable
    public String getNickname();

    /**
     * 性别
     *
     * @param <T> 表达性别的类型
     * @return 性别
     * @see spring.turbo.bean.Gender
     */
    @Nullable
    public <T> T getGender();

    @Nullable
    public <T> T getAvatar();

    @Nullable
    public <T> T getNativeUser();

    @Nullable
    public String getEmail();

    @Nullable
    public String getPhoneNumber();

    @Nullable
    public Date getDateOfBirth();

    @Nullable
    public String getBiography();

    @Nullable
    public Attributes getAttributes();

}
