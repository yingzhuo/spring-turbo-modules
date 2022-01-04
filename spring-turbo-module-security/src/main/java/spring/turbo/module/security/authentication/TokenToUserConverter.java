/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.webmvc.token.StringToken;
import spring.turbo.webmvc.token.Token;

/**
 * {@link Token} 对象到 {@link UserDetails} 的转换器
 *
 * @author 应卓
 * @see Token
 * @see StringToken
 * @see UserDetails
 * @see spring.turbo.module.security.user.UserDetailsPlus
 * @since 1.0.0
 */
@FunctionalInterface
public interface TokenToUserConverter extends Converter<Token, UserDetails> {

    /**
     * 将令牌转换为UserDetails
     *
     * @param token 令牌实体
     * @return {@link UserDetails} 实例，为 {@code null}时，等同于认证失败
     * @throws AuthenticationException 认证失败
     */
    @Nullable
    @Override
    public UserDetails convert(@Nullable Token token) throws AuthenticationException;

    /**
     * 将令牌转换为UserDetails
     *
     * @param rawToken 令牌字符串
     * @return {@link UserDetails} 实例，为 {@code null}时，等同于认证失败
     * @throws AuthenticationException 认证失败
     */
    @Nullable
    public default UserDetails convert(String rawToken) throws AuthenticationException {
        return convert(StringToken.of(rawToken));
    }

}
