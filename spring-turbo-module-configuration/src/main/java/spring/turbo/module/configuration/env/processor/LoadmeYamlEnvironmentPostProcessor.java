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
import org.springframework.core.env.PropertySource;
import spring.turbo.module.configuration.util.PropertySourceUtils;

/**
 * @author 应卓
 * @since 2.2.1
 */
public class LoadmeYamlEnvironmentPostProcessor extends AbstractLoadmeEnvironmentPostProcessor {

    public LoadmeYamlEnvironmentPostProcessor(DeferredLogFactory logFactory, ConfigurableBootstrapContext bootstrapContext) {
        super(logFactory, bootstrapContext);
    }

    @Override
    protected void execute(ConfigurableEnvironment environment, SpringApplication application) {

        var option = LoadmeOption.YAML;

        if (handlingIsRequired()) {
            var pair = option.load(application);

            if (pair.nothingToRead()) {
                return;
            }

            PropertySource<?> fromClzPath = PropertySourceUtils.loadYamlFormat(pair.getClasspathResource(), LOADME + " (classpath)");
            PropertySource<?> fromAppHome = PropertySourceUtils.loadYamlFormat(pair.getApplicationHomeResource(), LOADME + " (application home)");

            environment.getPropertySources()
                    .addLast(fromAppHome);

            environment.getPropertySources()
                    .addLast(fromClzPath);

            handled(option);
        }
    }

}
