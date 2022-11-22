/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.customizer;

import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * @author 应卓
 * @see WebSecurity
 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
 * @since 1.2.3
 */
public class SpringSecurityDebugModeCustomizer implements WebSecurityCustomizer, Ordered {

    private final boolean debugMode;

    public SpringSecurityDebugModeCustomizer(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public void customize(WebSecurity web) {
        web.debug(debugMode);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
