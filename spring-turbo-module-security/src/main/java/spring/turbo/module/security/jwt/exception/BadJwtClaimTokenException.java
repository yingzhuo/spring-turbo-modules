package spring.turbo.module.security.jwt.exception;

import spring.turbo.module.security.exception.BadTokenException;

/**
 * 令牌携带的Claim错误
 *
 * @author 应卓
 * @since 3.3.2
 */
public class BadJwtClaimTokenException extends BadTokenException {

    public BadJwtClaimTokenException(String msg) {
        super(msg);
    }

    public BadJwtClaimTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
