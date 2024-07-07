package spring.turbo.module.configuration.env.processor;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;

/**
 * @author 应卓
 * @since 2.2.1
 */
abstract class AbstractLoadmeEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    protected static final String LOADME = "loadme";

    private final ConfigurableBootstrapContext boot;
    private int order = 0;

    public AbstractLoadmeEnvironmentPostProcessor(ConfigurableBootstrapContext boot) {
        this.boot = boot;
    }

    protected final boolean isNotHandled() {
        return !boot.isRegistered(LoadmeOption.class);
    }

    protected final void setHandled(LoadmeOption option) {
        boot.registerIfAbsent(LoadmeOption.class, context -> option);
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
