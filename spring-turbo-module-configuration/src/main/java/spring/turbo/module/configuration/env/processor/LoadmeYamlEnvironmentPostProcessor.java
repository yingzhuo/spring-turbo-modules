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
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import static spring.turbo.module.configuration.util.PropertySourceUtils.loadYamlFormat;

/**
 * @author 应卓
 * @since 2.2.1
 */
public class LoadmeYamlEnvironmentPostProcessor extends AbstractLoadmeEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public LoadmeYamlEnvironmentPostProcessor(DeferredLogFactory logFactory, ConfigurableBootstrapContext bootstrapContext) {
        super(logFactory, bootstrapContext);
        super.setOrder(HIGHEST_PRECEDENCE + 101);
    }

    @Override
    protected void execute(ConfigurableEnvironment environment, SpringApplication application) {

        var option = LoadmeOption.YAML;

        if (super.handlingIsRequired()) {
            var pair = option.load(application);

            if (pair.nothingToRead()) {
                return;
            }

            var propertySources = environment.getPropertySources();
            var fromClzPath = loadYamlFormat(pair.getClasspathResource(), LOADME + " (classpath)");
            var fromAppHome = loadYamlFormat(pair.getApplicationHomeResource(), LOADME + " (application home)");

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
                super.handled(option);
            }
        }
    }

}
