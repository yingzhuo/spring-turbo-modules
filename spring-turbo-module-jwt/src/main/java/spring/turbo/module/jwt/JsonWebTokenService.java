/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt;

import spring.turbo.module.jwt.factory.JsonWebTokenData;
import spring.turbo.module.jwt.factory.JsonWebTokenFactory;
import spring.turbo.module.jwt.validator.JsonWebTokenValidator;
import spring.turbo.module.jwt.validator.ValidatingResult;

/**
 * @author 应卓
 * @since 3.1.1
 */
public interface JsonWebTokenService extends JsonWebTokenFactory, JsonWebTokenValidator {

    @Override
    public String apply(JsonWebTokenData data);

    @Override
    public ValidatingResult validate(String token);

    @Override
    public default boolean validateAsBoolean(String token) {
        return validate(token ) == ValidatingResult.NO_PROBLEM;
    }

}
