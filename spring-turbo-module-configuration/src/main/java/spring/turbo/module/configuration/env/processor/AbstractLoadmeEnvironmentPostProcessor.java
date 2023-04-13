/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.env.processor;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import spring.turbo.core.env.EnvironmentPostProcessorSupport;

/**
 * @author 应卓
 * @since 2.2.1
 */
abstract class AbstractLoadmeEnvironmentPostProcessor extends EnvironmentPostProcessorSupport {

    protected static final String LOADME = "loadme";

    public AbstractLoadmeEnvironmentPostProcessor(DeferredLogFactory logFactory, ConfigurableBootstrapContext bootstrapContext) {
        super(logFactory, bootstrapContext);
    }

    @Override
    protected abstract void execute(ConfigurableEnvironment environment, SpringApplication application);

    protected final boolean handlingIsRequired() {
        return !bootstrapContext.isRegistered(LoadmeOption.class);
    }

    protected final void handled(LoadmeOption option) {
        bootstrapContext.registerIfAbsent(LoadmeOption.class, context -> option);
    }

}
