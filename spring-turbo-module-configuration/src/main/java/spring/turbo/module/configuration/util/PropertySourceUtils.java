/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.util;

import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import spring.turbo.io.IOExceptionUtils;
import spring.turbo.module.configuration.env.HoconPropertySourceLoader;
import spring.turbo.util.collection.CollectionUtils;

import java.io.IOException;

/**
 * @author 应卓
 *
 * @since 2.2.1
 */
public final class PropertySourceUtils {

    private static final PropertySourceLoader PROPERTIES_FORMAT_LOADER = new PropertiesPropertySourceLoader();
    private static final PropertySourceLoader YAML_FORMAT_LOADER = new YamlPropertySourceLoader();
    private static final PropertySourceLoader HOCON_FORMAT_LOADER = new HoconPropertySourceLoader();

    /**
     * 私有构造方法
     */
    private PropertySourceUtils() {
        super();
    }

    @Nullable
    public static PropertySource<?> loadPropertiesFormat(@Nullable Resource resource, String propertySourceName) {
        if (resource == null || !resource.exists() || !resource.isReadable()) {
            return null;
        }

        try {
            var list = PROPERTIES_FORMAT_LOADER.load(propertySourceName, resource);
            if (CollectionUtils.size(list) == 0) {
                return null;
            }
            if (CollectionUtils.size(list) == 1) {
                return list.get(0);
            } else {
                throw IOExceptionUtils.toUnchecked("multiple document is NOT supported yet.");
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public static PropertySource<?> loadYamlFormat(@Nullable Resource resource, String propertySourceName) {
        if (resource == null || !resource.exists() || !resource.isReadable()) {
            return null;
        }

        try {
            var list = YAML_FORMAT_LOADER.load(propertySourceName, resource);
            if (CollectionUtils.size(list) == 0) {
                return null;
            }
            if (CollectionUtils.size(list) == 1) {
                return list.get(0);
            } else {
                throw IOExceptionUtils.toUnchecked("multiple document is NOT supported yet.");
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    public static PropertySource<?> loadHoconFormat(@Nullable Resource resource, String propertySourceName) {
        if (resource == null || !resource.exists() || !resource.isReadable()) {
            return null;
        }

        try {
            var list = HOCON_FORMAT_LOADER.load(propertySourceName, resource);
            if (CollectionUtils.size(list) == 0) {
                return null;
            }
            if (CollectionUtils.size(list) == 1) {
                return list.get(0);
            } else {
                throw IOExceptionUtils.toUnchecked("multiple document is NOT supported yet.");
            }
        } catch (IOException e) {
            return null;
        }
    }

}
