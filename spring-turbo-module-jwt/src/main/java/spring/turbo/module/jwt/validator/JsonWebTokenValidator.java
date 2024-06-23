/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.validator;

/**
 * JWT验证器
 * <ul>
 *     <li>验证格式是否合法</li>
 *     <li>验证签名是否合法</li>
 *     <li>验证日期是否合法</li>
 * </ul>
 *
 * @author 应卓
 * @since 3.3.1
 */
@FunctionalInterface
public interface JsonWebTokenValidator {

    /**
     * 验证令牌
     *
     * @param token JWT令牌
     * @return 验证结果
     */
    public ValidatingResult validate(String token);

    /**
     * 验证令牌是否合法
     *
     * @param token JWT令牌
     * @return true代表合法，否则为false
     */
    public default boolean validateAsBoolean(String token) {
        return validate(token) == ValidatingResult.NO_PROBLEM;
    }

}
