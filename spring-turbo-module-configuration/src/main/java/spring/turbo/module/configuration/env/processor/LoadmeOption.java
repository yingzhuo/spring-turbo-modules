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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import spring.turbo.core.ResourceLoaders;
import spring.turbo.io.RichResource;

import java.util.Arrays;

import static spring.turbo.core.SpringApplicationUtils.getHomeDirAsString;
import static spring.turbo.util.StringFormatter.format;

/**
 * @author 应卓
 * @since 2.2.1
 */
public enum LoadmeOption {

    PROPERTIES(".properties", ".xml"), YAML(".yaml", ".yml"), HOCON(".conf");

    private final static ResourceLoader RESOURCE_LOADER = ResourceLoaders.getDefault();

    private final String[] suffixes;

    LoadmeOption(String... suffixes) {
        this.suffixes = suffixes;
    }

    public ResourcePair load(SpringApplication application) {
        var classPathResource = this.getClassPathResource();
        var applicationHomeResource = this.getApplicationHomeResource(application);
        return new ResourcePair(classPathResource, applicationHomeResource);
    }

    @Nullable
    private Resource getClassPathResource() {
        var locations = Arrays.stream(this.suffixes).map(suffix -> format("classpath:loadme{}", suffix)).toList();

        return RichResource.builder().resourceLoader(RESOURCE_LOADER).addLocations(locations).build().orElse(null);
    }

    @Nullable
    private Resource getApplicationHomeResource(final SpringApplication application) {
        var locations = Arrays.stream(this.suffixes)
                .map(suffix -> format("file:{}/loadme{}", getHomeDirAsString(application), suffix)).toList();

        return RichResource.builder().resourceLoader(RESOURCE_LOADER).addLocations(locations).build().orElse(null);
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
            if (applicationHomeResource != null && applicationHomeResource.exists()
                    && applicationHomeResource.isReadable()) {
                c++;
            }
            return c == 0;
        }
    }

}
