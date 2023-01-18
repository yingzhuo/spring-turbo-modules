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
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 2.0.9
 */
public class QueryVersionResolver implements VersionResolver {

    private final String parameterName;

    public QueryVersionResolver(String parameterName) {
        Asserts.hasText(parameterName);
        this.parameterName = parameterName;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        var version = request.getParameter(this.parameterName);
        if (version == null || version.isBlank()) {
            return null;
        } else {
            return version;
        }
    }

}
