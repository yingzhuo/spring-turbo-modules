/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

/**
 * @author 应卓
 * @see spring.turbo.module.security.FilterConfiguration
 * @see AbstractRequestLoggingFilter
 * @see CommonsRequestLoggingFilter
 * @see ServletContextRequestLoggingFilter
 * @see HumanReadableRequestLoggingFilter
 * @see spring.turbo.module.security.filter.factory.LoggingFilterFactoryBean
 * @since 1.0.0
 */
public class SimpleRequestLoggingFilter extends AbstractRequestLoggingFilter {

    /**
     * 默认构造方法
     */
    public SimpleRequestLoggingFilter() {
        setIncludeClientInfo(true);
        setIncludeHeaders(true);
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(100);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.debug(message);
    }

}
