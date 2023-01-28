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

/**
 * @author 应卓
 * @since 2.0.10
 */
public class ServletPathVersionResolver implements VersionResolver {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String PATTERN = "/{version:[vV][0-9]+}/**";

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        try {
            var servletPath = request.getServletPath();
            var variables = PATH_MATCHER.extractUriTemplateVariables(PATTERN, servletPath);
            return variables.get("version");
        } catch (Throwable e) {
            return null;
        }
    }

}
