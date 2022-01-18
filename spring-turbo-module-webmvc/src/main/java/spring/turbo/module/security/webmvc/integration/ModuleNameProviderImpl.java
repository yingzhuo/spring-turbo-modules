/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.webmvc.integration;

import org.springframework.lang.NonNull;
import spring.turbo.integration.ModuleNameProvider;

import static spring.turbo.integration.Modules.SPRING_TURBO_WEBMVC;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class ModuleNameProviderImpl implements ModuleNameProvider {

    @NonNull
    @Override
    public String getModuleName() {
        return SPRING_TURBO_WEBMVC.getName();
    }

}
