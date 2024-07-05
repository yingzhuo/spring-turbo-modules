package spring.turbo.module.security.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.token.StringToken;
import spring.turbo.module.security.token.Token;

/**
 * {@link Token} 对象到 {@link UserDetails} 的转换器
 *
 * @author 应卓
 * @see Token
 * @see StringToken
 * @see UserDetails
 * @see spring.turbo.module.security.user.UserDetailsPlus
 * @see org.springframework.security.core.userdetails.AuthenticationUserDetailsService
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

}
