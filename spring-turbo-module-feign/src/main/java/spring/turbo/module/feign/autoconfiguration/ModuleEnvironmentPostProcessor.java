/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign.autoconfiguration;

import org.springframework.core.Ordered;
import spring.turbo.core.env.AbstractModuleEnvironmentPostProcessor;

/**
 * @author 应卓
 * @since 2.0.7
 */
public class ModuleEnvironmentPostProcessor extends AbstractModuleEnvironmentPostProcessor {

    /**
     * 默认构造方法
     */
    public ModuleEnvironmentPostProcessor() {
        super(Ordered.LOWEST_PRECEDENCE - 200, "spring-turbo-feign", "spring-turbo-module-feign");
    }

}
