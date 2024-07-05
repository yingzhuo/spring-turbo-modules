package spring.turbo.module.jwt.factory;

/**
 * JWT令牌生成器
 *
 * @author 应卓
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
