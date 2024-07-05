package spring.turbo.module.security.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * @author 应卓
 * @since 2.0.5
 */
public class BadTokenException extends AccountStatusException {

    public BadTokenException(String msg) {
        super(msg);
    }

    public BadTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
