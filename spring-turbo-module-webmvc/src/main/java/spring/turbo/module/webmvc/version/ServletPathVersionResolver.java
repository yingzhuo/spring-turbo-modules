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

import static spring.turbo.util.StringUtils.isNotBlank;

/**
 * @author 应卓
 * @since 2.0.10
 */
public class ServletPathVersionResolver implements VersionResolver {

    private static final PathMatcher MATCHER = new AntPathMatcher();
    private static final String DEFAULT_PATTERN = "/{version:[vV][0-9]+}/**";
    private static final String DEFAULT_VARIABLE = "version";

    private final String antStylePattern;
    private final String variable;

    public ServletPathVersionResolver() {
        this(DEFAULT_PATTERN, DEFAULT_VARIABLE);
    }

    public ServletPathVersionResolver(String antStylePattern, String variable) {
        Asserts.hasText(antStylePattern);
        Asserts.hasText(variable);
        this.antStylePattern = antStylePattern;
        this.variable = variable;
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        try {
            var servletPath = request.getServletPath();
            var variables = MATCHER.extractUriTemplateVariables(antStylePattern, servletPath);
            var version = variables.get(variable);
            return isNotBlank(version) ? version : null;
        } catch (Throwable e) {
            return null;
        }
    }

}
