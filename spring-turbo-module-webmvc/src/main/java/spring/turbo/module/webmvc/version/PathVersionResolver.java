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
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import spring.turbo.util.Asserts;

import static spring.turbo.util.StringPool.SLASH;

/**
 * @author 应卓
 * @since 2.0.9
 */
public class PathVersionResolver implements VersionResolver {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String PATTERN_SUFFIX = "/{version:.*}/**";

    private final String contextPath;

    public PathVersionResolver(String contextPath) {
        this.contextPath = betterContextPath(contextPath);
    }

    private String betterContextPath(String contextPath) {
        Asserts.notNull(contextPath);

        if (contextPath.isBlank()) {
            return SLASH;
        }

        if (!SLASH.equals(contextPath) && contextPath.endsWith(SLASH)) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        var path = request.getRequestURI();

        String patternToUse = null;

        if (this.contextPath.equals(SLASH)) {
            patternToUse = PATTERN_SUFFIX;
        } else {
            patternToUse = contextPath + PATTERN_SUFFIX;
        }

        var map = PATH_MATCHER.extractUriTemplateVariables(patternToUse, path);
        var version = map.get("version");

        if (version == null || version.isBlank()) {
            return null;
        } else {
            return version;
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
