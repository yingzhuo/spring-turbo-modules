/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.bean.Attributes;

import java.util.Date;

/**
 * 增强型 {@link UserDetails}
 * <p>
 * 为 {@link UserDetails} 增加若干方法使之更方便。
 *
 * @author 应卓
 * @see UserDetails
 * @see UserDetailsPlusBuilder
 * @see UserDetailsPlusImpl
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

    public <T> T getId();

    public String getNickname();

    public <T> T getGender();

    public <T> T getAvatar();

    public <T> T getNativeUser();

    public String getEmail();

    public String getPhoneNumber();

    public Date getDateOfBirth();

    public String getBiography();

    public Attributes getAttributes();

}
