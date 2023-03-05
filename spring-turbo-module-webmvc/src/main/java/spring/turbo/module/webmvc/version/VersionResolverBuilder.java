/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.version;

import spring.turbo.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 应卓
 * @see CompositeVersionResolver
 * @since 2.0.9
 */
public final class VersionResolverBuilder {

    private final List<VersionResolver> resolvers = new ArrayList<>();

    /**
     * 构造方法
     */
    VersionResolverBuilder() {
        super();
    }

    public VersionResolverBuilder fromServletPath() {
        resolvers.add(new ServletPathVersionResolver());
        return this;
    }

    public VersionResolverBuilder fromHttpHeader(String headerName) {
        resolvers.add(new HeaderVersionResolver(headerName));
        return this;
    }

    public VersionResolverBuilder fromHttpHeaderWithDefaults() {
        resolvers.add(new HeaderVersionResolver());
        return this;
    }

    public VersionResolverBuilder fromHttpQuery(String parameterName) {
        resolvers.add(new QueryVersionResolver(parameterName));
        return this;
    }

    public VersionResolverBuilder fromHttpQueryWithDefaults() {
        resolvers.add(new QueryVersionResolver());
        return this;
    }

    public VersionResolverBuilder add(Collection<VersionResolver> resolvers) {
        CollectionUtils.nullSafeAddAll(this.resolvers, resolvers);
        return this;
    }

    public VersionResolverBuilder add(VersionResolver... resolvers) {
        CollectionUtils.nullSafeAddAll(this.resolvers, resolvers);
        return this;
    }

    public VersionResolver build() {
        return new CompositeVersionResolver(resolvers);
    }

}
