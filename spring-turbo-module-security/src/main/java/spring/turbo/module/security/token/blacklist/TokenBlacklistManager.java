package spring.turbo.module.security.token.blacklist;

import spring.turbo.module.security.exception.BlacklistTokenException;
import spring.turbo.module.security.token.Token;

/**
 * @author 应卓
 * @since 2.0.5
 */
public interface TokenBlacklistManager {

    public void save(Token token);

    public void verify(Token token) throws BlacklistTokenException;

}
