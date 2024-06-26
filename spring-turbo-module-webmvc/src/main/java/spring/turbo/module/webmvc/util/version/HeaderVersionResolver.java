/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.util.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import static spring.turbo.util.StringUtils.isNotBlank;

/**
 * @author 应卓
 * @since 2.0.9
 */
public class HeaderVersionResolver implements VersionResolver {

    private static final String DEFAULT_HEADER_NAME = "X-Api-Version";

    private final String headerName;

    /**
     * 默认构造方法
     */
    public HeaderVersionResolver() {
        this(DEFAULT_HEADER_NAME);
    }

    public HeaderVersionResolver(String headerName) {
        Asserts.hasText(headerName);
        this.headerName = headerName;
    }

    @Nullable
    @Override
    public String resolve(HttpServletRequest request) {
        try {
            var version = request.getHeader(this.headerName);
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
