/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dataaccessing.autoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import spring.turbo.core.AbstractResourceBasedEnvironmentPostProcessor;
import spring.turbo.io.ResourceOption;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.StringFormatter;

import java.util.ArrayList;

import static spring.turbo.core.Dependencies.*;

/**
 * @author 应卓
 * @since 2.0.7
 */
public class ModuleEnvironmentPostProcessor extends AbstractResourceBasedEnvironmentPostProcessor {

    /**
     * 默认构造方法
     */
    public ModuleEnvironmentPostProcessor() {
        super(Ordered.LOWEST_PRECEDENCE - 200);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final ResourceOption resourceOption = loadResource(application);

        if (resourceOption.isAbsent()) {
            return;
        }

        final var propertySource = toPropertySource(resourceOption);
        if (propertySource != null) {
            final var propertySources = environment.getPropertySources();
            propertySources.addLast(propertySource);
        }
    }

    private ResourceOption loadResource(SpringApplication application) {
        final var resourceLocations = new ArrayList<String>();

        // ---
        for (var appDir : getApplicationDirectories(application)) {
            resourceLocations.add(StringFormatter.format("file:{}/spring-turbo-module-data-accessing.properties", appDir));
            resourceLocations.add(StringFormatter.format("file:{}/spring-turbo-module-data-accessing.xml", appDir));
            if (YAML_PRESENT) {
                resourceLocations.add(StringFormatter.format("file:{}/spring-turbo-module-data-accessing.yaml", appDir));
                resourceLocations.add(StringFormatter.format("file:{}/spring-turbo-module-data-accessing.yml", appDir));
            }
            if (HOCON_PRESENT) {
                resourceLocations.add(StringFormatter.format("file:{}/spring-turbo-module-data-accessing.conf", appDir));
            }
            if (TOML_PRESENT) {
                resourceLocations.add(StringFormatter.format("file:{}/spring-turbo-module-data-accessing.toml", appDir));
            }
        }

        // ---
        resourceLocations.add("classpath:spring-turbo-module-data-accessing.properties");
        resourceLocations.add("classpath:spring-turbo-module-data-accessing.xml");
        if (YAML_PRESENT) {
            resourceLocations.add("classpath:spring-turbo-module-data-accessing.yaml");
            resourceLocations.add("classpath:spring-turbo-module-data-accessing.yml");
        }
        if (HOCON_PRESENT) {
            resourceLocations.add("classpath:spring-turbo-module-data-accessing.conf");
        }
        if (TOML_PRESENT) {
            resourceLocations.add("classpath:spring-turbo-module-data-accessing.toml");
        }

        // ---
        resourceLocations.add("classpath:META-INF/spring-turbo-module-data-accessing.properties");
        resourceLocations.add("classpath:META-INF/spring-turbo-module-data-accessing.xml");
        if (YAML_PRESENT) {
            resourceLocations.add("classpath:META-INF/spring-turbo-module-data-accessing.yaml");
            resourceLocations.add("classpath:META-INF/spring-turbo-module-data-accessing.yml");
        }
        if (HOCON_PRESENT) {
            resourceLocations.add("classpath:META-INF/spring-turbo-module-data-accessing.conf");
        }
        if (TOML_PRESENT) {
            resourceLocations.add("classpath:META-INF/spring-turbo-module-data-accessing.toml");
        }

        // ---
        resourceLocations.add("classpath:conf/spring-turbo-module-data-accessing.properties");
        resourceLocations.add("classpath:conf/spring-turbo-module-data-accessing.xml");
        if (YAML_PRESENT) {
            resourceLocations.add("classpath:conf/spring-turbo-module-data-accessing.yaml");
            resourceLocations.add("classpath:conf/spring-turbo-module-data-accessing.yml");
        }
        if (HOCON_PRESENT) {
            resourceLocations.add("classpath:conf/spring-turbo-module-data-accessing.conf");
        }
        if (TOML_PRESENT) {
            resourceLocations.add("classpath:conf/spring-turbo-module-data-accessing.toml");
        }

        return ResourceOptions.builder()
                .add(resourceLocations)
                .build();
    }

}
