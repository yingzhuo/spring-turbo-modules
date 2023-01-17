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

/**
 * @author 应卓
 * @since 2.0.9
 */
public class RequestVersionResolver implements VersionResolver {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String TEMPLATE_SUFFIX = "/{version:.*}/**";

    private final String contextPath;

    public RequestVersionResolver(String contextPath) {
        Asserts.hasText(contextPath);
        this.contextPath = contextPath;
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        var path = request.getRequestURI();

        String templateToUse = null;
        if (this.contextPath.equals("/")) {
            templateToUse = TEMPLATE_SUFFIX;
        } else {
            templateToUse = contextPath + TEMPLATE_SUFFIX;
        }

        var map = PATH_MATCHER.extractUriTemplateVariables(templateToUse, path);
        var version = map.get("version");
        return version != null ? version.trim() : null;
    }

}
