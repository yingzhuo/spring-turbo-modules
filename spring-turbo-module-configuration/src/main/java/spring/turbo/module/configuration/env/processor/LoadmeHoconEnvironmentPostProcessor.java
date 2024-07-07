package spring.turbo.module.configuration.env.processor;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import static spring.turbo.module.configuration.util.PropertySourceUtils.loadHoconFormat;

/**
 * @author 应卓
 * @since 2.2.1
 */
@SuppressWarnings("DuplicatedCode")
public class LoadmeHoconEnvironmentPostProcessor extends AbstractLoadmeEnvironmentPostProcessor {

    public LoadmeHoconEnvironmentPostProcessor(ConfigurableBootstrapContext bootstrapContext) {
        super(bootstrapContext);
        super.setOrder(HIGHEST_PRECEDENCE + 102);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final var option = LoadmeOption.HOCON;

        if (super.isNotHandled()) {
            var pair = option.load(application);

            if (pair.nothingToRead()) {
                return;
            }

            var propertySources = environment.getPropertySources();
            var fromClzPath = loadHoconFormat(pair.getClasspathResource(), LOADME + " (classpath)");
            var fromAppHome = loadHoconFormat(pair.getApplicationHomeResource(), LOADME + " (application home)");

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
