/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.autoconfiguration;

import cn.hutool.jwt.signers.JWTSigner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.security.jwt.JwtTokenFactory;
import spring.turbo.module.security.jwt.JwtTokenFactoryImpl;

/**
 * @author 应卓
 *
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnClass(name = "cn.hutool.jwt.signers.JWTSigner")
@ConditionalOnBean(type = "cn.hutool.jwt.signers.JWTSigner")
@ConditionalOnMissingBean(type = "spring.turbo.module.security.jwt.JwtTokenFactory")
public class JwtTokenFactoryAutoConfiguration {

    /**
     * 默认构造方法
     */
    public JwtTokenFactoryAutoConfiguration() {
        super();
    }

    @Bean(name = "defaultJwtTokenFactory")
    public JwtTokenFactory jwtTokenFactory(JWTSigner signer) {
        return new JwtTokenFactoryImpl(signer);
    }

}
