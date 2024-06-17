/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.module.security.jwt.exception.BadJwtAlgorithmTokenException;
import spring.turbo.module.security.jwt.exception.BadJwtFormatTokenException;
import spring.turbo.module.security.jwt.exception.BadJwtTimeTokenException;
import spring.turbo.module.security.token.Token;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @see JwtDecorator
 * @since 2.2.4
 */
public abstract class AbstractJwtTokenToUserConverter implements TokenToUserConverter {

    private final JWTSigner signer;

    /**
     * 构造方法
     *
     * @param signer 签名器
     */
    protected AbstractJwtTokenToUserConverter(JWTSigner signer) {
        Asserts.notNull(signer);
        this.signer = signer;
    }

    @Nullable
    @Override
    public final UserDetails convert(@Nullable Token token) throws AuthenticationException {

        if (token == null) {
            return null;
        }

        var rawToken = token.asString();

        JWT jwt;

        try {
            jwt = JWT.of(rawToken);
        } catch (Exception e) {
            // 根本不是JWT格式
            throw new BadJwtFormatTokenException(e.getMessage(), e);
        }

        var validator = JWTValidator.of(jwt);

        try {
            validator.validateAlgorithm(signer);
        } catch (ValidateException e) {
            // 签名错误
            throw new BadJwtAlgorithmTokenException(e.getMessage(), e);
        }

        try {
            validator.validateDate();
        } catch (ValidateException e) {
            // 日期错误
            throw new BadJwtTimeTokenException(e.getMessage(), e);
        }

        return doAuthenticate(rawToken, jwt);
    }

    @Nullable
    protected abstract UserDetails doAuthenticate(String rawToken, JWT jwt) throws AuthenticationException;

}
