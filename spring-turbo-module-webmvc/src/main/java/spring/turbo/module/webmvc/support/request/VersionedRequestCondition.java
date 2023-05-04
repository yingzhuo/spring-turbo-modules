/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.support.request;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import spring.turbo.module.webmvc.version.VersionResolver;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 *
 * @since 2.0.9
 */
public class VersionedRequestCondition implements RequestCondition<VersionedRequestCondition> {

    private final VersionResolver versionResolver;
    private final String acceptVersion;
    private final boolean ignoreCase;

    public VersionedRequestCondition(VersionResolver versionResolver, String acceptVersion, boolean ignoreCase) {
        Asserts.hasText(acceptVersion);
        Asserts.notNull(versionResolver);
        this.versionResolver = versionResolver;
        this.acceptVersion = acceptVersion.trim();
        this.ignoreCase = ignoreCase;
    }

    @Override
    public VersionedRequestCondition combine(VersionedRequestCondition other) {
        return this;
    }

    @Override
    public int compareTo(VersionedRequestCondition other, HttpServletRequest request) {
        return 0;
    }

    @Nullable
    @Override
    public VersionedRequestCondition getMatchingCondition(HttpServletRequest request) {
        var versionString = versionResolver.resolve(request);
        if (versionString == null) {
            return null;
        }

        if (ignoreCase) {
            return versionString.equalsIgnoreCase(this.acceptVersion) ? this : null;
        } else {
            return versionString.equals(this.acceptVersion) ? this : null;
        }
    }

}
