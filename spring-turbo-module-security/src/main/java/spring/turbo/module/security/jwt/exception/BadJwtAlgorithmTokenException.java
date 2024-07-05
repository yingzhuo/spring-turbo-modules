package spring.turbo.module.security.jwt.exception;

import spring.turbo.module.security.exception.BadTokenException;

/**
 * 错误的令牌签名
 *
 * @author 应卓
 * @since 2.2.4
 */
public class BadJwtAlgorithmTokenException extends BadTokenException {

    public BadJwtAlgorithmTokenException(String msg) {
        super(msg);
    }

    public BadJwtAlgorithmTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
