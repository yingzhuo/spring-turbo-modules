/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import spring.turbo.util.Asserts;

/**
 * JWT令牌工厂
 *
 * @author 应卓
 * @since 1.0.0
 */
@FunctionalInterface
public interface JwtTokenFactory {

    /**
     * 创建令牌
     *
     * @param metadata 令牌元数据信息
     * @return 令牌字符串
     * @see JwtTokenMetadata#builder()
     * @see JwtTokenMetadata.Builder
     */
    public String create(JwtTokenMetadata metadata);

    /**
     * 创建令牌
     *
     * @param builder 令牌元数据创建器
     * @return 令牌字符串
     */
    public default String create(JwtTokenMetadata.Builder builder) {
        Asserts.notNull(builder);
        return create(builder.build());
    }

}
