/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import spring.turbo.module.security.exception.InvalidSignatureException;

/**
 * 内部工具，完成JWT相关异常转换
 *
 * @author 应卓
 * @since 1.0.4
 */
final class VerificationExceptionTransformer {

    private VerificationExceptionTransformer() {
        super();
    }

    @NonNull
    public static AuthenticationException transform(@NonNull JWTVerificationException e) {
        if (e instanceof TokenExpiredException) {
            // 令牌过期
            return new CredentialsExpiredException(e.getMessage());
        }
        if (e instanceof AlgorithmMismatchException) {
            // 签名算法不匹配
            throw new InvalidSignatureException(e.getMessage());
        }
        if (e instanceof SignatureVerificationException) {
            // 签名被篡改
            throw new InvalidSignatureException(e.getMessage());
        }
        return new BadCredentialsException(e.getMessage());
    }

}
