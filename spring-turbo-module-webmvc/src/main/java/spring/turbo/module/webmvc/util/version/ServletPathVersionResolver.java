package spring.turbo.module.webmvc.util.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

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

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
