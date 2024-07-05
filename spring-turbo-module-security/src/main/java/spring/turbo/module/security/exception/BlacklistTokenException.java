package spring.turbo.module.security.exception;

/**
 * 令牌已被加入黑名单 不可以再使用
 *
 * @author 应卓
 * @see spring.turbo.module.security.token.blacklist.TokenBlacklistManager
 * @since 2.0.5
 */
public class BlacklistTokenException extends BadTokenException {

    public BlacklistTokenException(String msg) {
        super(msg);
    }

    public BlacklistTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
