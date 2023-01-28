/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.version;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.OrderComparator;
import spring.turbo.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 应卓
 * @since 2.0.9
 */
@Slf4j
public class CompositeVersionResolver implements VersionResolver {

    private final List<VersionResolver> resolvers = new ArrayList<>();

    public CompositeVersionResolver(VersionResolver... resolvers) {
        CollectionUtils.nullSafeAddAll(this.resolvers, resolvers);
        OrderComparator.sort(this.resolvers);
    }

    public CompositeVersionResolver(Collection<VersionResolver> resolvers) {
        CollectionUtils.nullSafeAddAll(this.resolvers, resolvers);
        OrderComparator.sort(this.resolvers);
    }

    @Override
    public String resolve(HttpServletRequest request) {
        for (var resolver : resolvers) {
            try {
                var version = resolver.resolve(request);
                if (version != null) {
                    return version;
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
                continue;
            }
        }
        return null;
    }

}
