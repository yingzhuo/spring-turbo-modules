package spring.turbo.module.jwt;

import org.springframework.lang.Nullable;

/**
 * Json Web Token 服务 <br>
 * 核心服务
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
     * @param token         令牌
     * @param jwtAssertions Claim验证
     * @return 验证结果
     * @see ValidatingResult
     */
    public ValidatingResult validateToken(String token, @Nullable JwtAssertions jwtAssertions);

    /**
     * 验证令牌是否合法
     *
     * @param token 令牌
     * @return 验证结果
     * @see ValidatingResult
     */
    public default ValidatingResult validateToken(String token) {
        return validateToken(token, null);
    }

    /**
     * 验证令牌是否合法
     *
     * @param token         令牌
     * @param jwtAssertions Claim验证
     * @return 合法时返回true
     * @see ValidatingResult
     */
    public default boolean validateTokenAsBoolean(String token, @Nullable JwtAssertions jwtAssertions) {
        return validateToken(token, jwtAssertions) == ValidatingResult.OK;
    }

    /**
     * 验证令牌是否合法
     *
     * @param token 令牌
     * @return 合法时返回true
     * @see ValidatingResult
     */
    public default boolean validateTokenAsBoolean(String token) {
        return validateTokenAsBoolean(token, null);
    }
}
