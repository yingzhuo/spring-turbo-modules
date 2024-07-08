package spring.turbo.module.jwt.factory;

/**
 * JWT令牌生成器
 *
 * @author 应卓
 * @see spring.turbo.module.jwt.validator.JsonWebTokenValidator
 * @since 3.1.1
 */
@FunctionalInterface
public interface JsonWebTokenFactory {

    /**
     * 生成令牌
     *
     * @param data 令牌信息
     * @return JWT令牌
     */
    public String apply(JsonWebTokenData data);

}
