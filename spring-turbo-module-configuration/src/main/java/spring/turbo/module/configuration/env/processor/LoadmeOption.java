package spring.turbo.module.configuration.env.processor;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import spring.turbo.core.ResourceUtils;

import java.util.Arrays;

import static spring.turbo.util.StringFormatter.format;
import static spring.turbo.util.collection.CollectionUtils.size;

/**
 * @author 应卓
 * @since 2.2.1
 */
public enum LoadmeOption {

    PROPERTIES(".properties", ".xml"),
    YAML(".yaml", ".yml"),
    HOCON(".conf");

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
        var locations = Arrays.stream(this.suffixes)
                .map(suffix -> format("classpath:loadme{}", suffix))
                .toList();
        return ResourceUtils.loadFirstExistsResource(locations);
    }

    @Nullable
    private Resource getApplicationHomeResource(final SpringApplication application) {
        var locations = Arrays.stream(this.suffixes)
                .map(suffix -> format("file:{}/loadme{}", getAppHomeDir(application), suffix))
                .toList();
        return ResourceUtils.loadFirstExistsResource(locations);
    }

    private String getAppHomeDir(SpringApplication springApplication) {
        var sourceClasses = springApplication.getAllSources()
                .stream()
                .filter(o -> o instanceof Class<?>)
                .map(o -> (Class<?>) o)
                .toList();

        var file = size(sourceClasses) == 1 ?
                new ApplicationHome(sourceClasses.get(0)).getDir() :
                new ApplicationHome().getDir();

        return file.toPath().toAbsolutePath().toString();
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
