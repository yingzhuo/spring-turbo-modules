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

import static spring.turbo.module.configuration.util.PropertySourceUtils.loadPropertiesFormat;

/**
 * @author 应卓
 * @since 2.2.1
 */
public class LoadmePropertiesEnvironmentPostProcessor extends AbstractLoadmeEnvironmentPostProcessor {

    public LoadmePropertiesEnvironmentPostProcessor(DeferredLogFactory logFactory,
                                                    ConfigurableBootstrapContext bootstrapContext) {
        super(logFactory, bootstrapContext);
        super.setOrder(HIGHEST_PRECEDENCE + 100);
    }

    @Override
    protected void execute(ConfigurableEnvironment environment, SpringApplication application) {

        final var option = LoadmeOption.PROPERTIES;

        if (super.isNotHandled()) {
            var pair = option.load(application);

            if (pair.nothingToRead()) {
                return;
            }

            var propertySources = environment.getPropertySources();
            var fromClzPath = loadPropertiesFormat(pair.getClasspathResource(), LOADME + " (classpath)");
            var fromAppHome = loadPropertiesFormat(pair.getApplicationHomeResource(), LOADME + " (application home)");

            int addedCount = 0;
            if (fromAppHome != null) {
                propertySources.addLast(fromAppHome);
                addedCount++;
            }

            if (fromClzPath != null) {
                propertySources.addLast(fromClzPath);
                addedCount++;
            }

            if (addedCount > 0) {
                super.setHandled(option);
            }
        }
    }

}
