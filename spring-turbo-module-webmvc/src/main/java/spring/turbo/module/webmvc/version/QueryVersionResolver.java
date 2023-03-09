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
import spring.turbo.util.Asserts;

import static spring.turbo.util.StringUtils.isNotBlank;

/**
 * @author 应卓
 * @since 2.0.9
 */
public class QueryVersionResolver implements VersionResolver {

    public static final String DEFAULT_PARAMETER_NAME = "_api_version";

    private final String parameterName;

    /**
     * 默认构造方法
     */
    public QueryVersionResolver() {
        this(DEFAULT_PARAMETER_NAME);
    }

    public QueryVersionResolver(String parameterName) {
        Asserts.hasText(parameterName);
        this.parameterName = parameterName;
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        try {
            var version = request.getParameter(this.parameterName);
            return isNotBlank(version) ? version : null; // blank -> null
        } catch (Throwable e) {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 100;
    }

}
