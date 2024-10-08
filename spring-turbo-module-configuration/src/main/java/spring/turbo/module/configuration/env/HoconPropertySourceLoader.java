package spring.turbo.module.configuration.env;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.*;

/**
 * @author 应卓
 * @since 2.1.3
 */
@SuppressWarnings("unchecked")
public class HoconPropertySourceLoader implements PropertySourceLoader {

    @Override
    public String[] getFileExtensions() {
        return new String[]{"conf"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        Config config = ConfigFactory.parseURL(resource.getURL());

        final Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap(result, config.root().unwrapped(), null);
        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new MapPropertySource(name, result));
    }

    private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String root) {
        boolean rootHasText = (null != root && !root.trim().isEmpty());

        source.forEach((key, value) -> {
            String path;

            if (rootHasText) {
                if (key.startsWith("[")) {
                    path = root + key;
                } else {
                    path = root + "." + key;
                }
            } else {
                path = key;
            }

            if (value instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) value;
                buildFlattenedMap(result, map, path);
            } else if (value instanceof Collection) {
                Collection<Object> collection = (Collection<Object>) value;
                int count = 0;
                for (Object object : collection) {
                    buildFlattenedMap(result, Collections.singletonMap("[" + (count++) + "]", object), path);
                }
            } else {
                result.put(path, null == value ? "" : value);
            }
        });
    }

}
