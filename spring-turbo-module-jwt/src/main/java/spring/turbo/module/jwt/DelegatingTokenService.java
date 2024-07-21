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
public final record DelegatingTokenService(
        JsonWebTokenFactory factory,
        JsonWebTokenValidator validator) implements JsonWebTokenService {

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(JsonWebTokenData data) {
        return factory.apply(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidatingResult validate(String token) {
        return validator.validate(token);
    }

}
