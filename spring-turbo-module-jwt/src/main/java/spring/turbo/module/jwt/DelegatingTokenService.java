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
 * 简单代理实现
 *
 * @author 应卓
 * @since 3.3.1
 */
public record DelegatingTokenService(
        JsonWebTokenFactory factory,
        JsonWebTokenValidator validator) implements JsonWebTokenService {

    @Override
    public String apply(JsonWebTokenData data) {
        return factory.apply(data);
    }

    @Override
    public ValidatingResult validate(String token) {
        return validator.validate(token);
    }

}
