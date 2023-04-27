/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.env.processor;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

import static spring.turbo.core.SpringApplicationUtils.getHomePath;
import static spring.turbo.util.StringFormatter.format;

/**
 * @author 应卓
 * @since 2.2.1
 */
public enum LoadmeOption {

    PROPERTIES(".properties"),
    YAML(".yaml"),
    HOCON(".conf");

    private final String suffix;
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    LoadmeOption(String suffix) {
        this.suffix = suffix;
    }

    public ResourcePair load(SpringApplication application) {

        var classPathResource = resourceLoader.getResource(
                format("classpath:loadme{}", suffix)
        );

        var applicationHomeResource = resourceLoader.getResource(
                format("file:{}/loadme{}", getHomePath(application), suffix)
        );

        return new ResourcePair(classPathResource, applicationHomeResource);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class ResourcePair {

        @Nullable
        private Resource classpathResource;

        @Nullable
        private Resource applicationHomeResource;

        public boolean nothingToRead() {
            int c = 0;
            if (classpathResource != null && classpathResource.exists() && classpathResource.isReadable()) {
                c++;
            }
            if (applicationHomeResource != null && applicationHomeResource.exists() && applicationHomeResource.isReadable()) {
                c++;
            }
            return c == 0;
        }
    }

}
