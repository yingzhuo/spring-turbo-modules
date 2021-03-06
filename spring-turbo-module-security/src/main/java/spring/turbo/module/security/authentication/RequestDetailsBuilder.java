/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.webmvc.HttpRequestSnapshot;

import javax.servlet.http.HttpServletRequest;

/**
 * {@code RequestDetails} 创建器
 * <p>
 * 一般说来，RequestDetails是对一个HTTP请求的简要描述，类型一般为{@link String}。
 *
 * @author 应卓
 * @since 1.0.4
 */
@FunctionalInterface
public interface RequestDetailsBuilder {

    /**
     * HTTP快照
     */
    public static final RequestDetailsBuilder SNAPSHOT = request -> HttpRequestSnapshot.of(request).toString();

    /**
     * SpringSecurity默认实现
     */
    public static final RequestDetailsBuilder SPRING_SECURITY_DEFAULT = request -> new WebAuthenticationDetailsSource().buildDetails(request);

    /**
     * HTTP信息简要描述
     */
    public static final RequestDetailsBuilder DESCRIPTION = request -> new ServletWebRequest(request).getDescription(true);

    /**
     * 创建Details
     *
     * @param request HTTP请求
     * @return details或{@code null}
     */
    @Nullable
    public Object getDetails(HttpServletRequest request);

}
