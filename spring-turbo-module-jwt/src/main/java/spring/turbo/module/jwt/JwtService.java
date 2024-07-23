package spring.turbo.module.jwt;

/**
 * Json Web Token 服务
 *
 * @author 应卓
 * @since 3.1.1
 */
public interface JwtService {

    /**
     * 生成JWT令牌
     *
     * @param data 令牌数据
     * @return JWT令牌
     */
    public String createToken(JwtData data);

    /**
     * 验证令牌是否合法
     *
     * @param token 令牌
     * @return 验证结果
     * @see ValidatingResult
     */
    public ValidatingResult validateToken(String token);

    /**
     * 验证令牌是否合法
     *
     * @param token 令牌
     * @return 合法时返回true
     * @see ValidatingResult
     */
    public default boolean validateTokenAsBoolean(String token) {
        return validateToken(token) == ValidatingResult.OK;
    }

}
