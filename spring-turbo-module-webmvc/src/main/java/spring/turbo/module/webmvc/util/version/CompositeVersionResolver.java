package spring.turbo.module.webmvc.util.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.core.OrderComparator.sort;
import static spring.turbo.util.collection.CollectionUtils.nullSafeAddAll;

/**
 * @author 应卓
 * @see #getDefault()
 * @since 2.0.9
 */
public class CompositeVersionResolver implements VersionResolver, InitializingBean {

    private final List<VersionResolver> resolvers = new ArrayList<>();

    public CompositeVersionResolver(VersionResolver... resolvers) {
        nullSafeAddAll(this.resolvers, resolvers);
        sort(this.resolvers);
        afterPropertiesSet();
    }

    public CompositeVersionResolver(Collection<VersionResolver> resolvers) {
        nullSafeAddAll(this.resolvers, resolvers);
        sort(this.resolvers);
        afterPropertiesSet();
    }

    public static CompositeVersionResolver getDefault() {
        return new CompositeVersionResolver(
                new ServletPathVersionResolver(),
                new HeaderVersionResolver(),
                new QueryVersionResolver()
        );
    }

    @Override
    public void afterPropertiesSet() {
        if (resolvers.isEmpty()) {
            resolvers.add(NullVersionResolver.getInstance());
        }
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        for (var resolver : resolvers) {
            try {
                var version = resolver.resolve(request);
                if (version != null) {
                    return version;
                }
            } catch (Exception ignored) {
                // noop
            }
        }
        return null;
    }

}
