/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.module.security.authentication.details.AuthenticationDetailsImpl;
import spring.turbo.module.security.token.Token;
import spring.turbo.webmvc.HttpRequestSnapshot;

/**
 * {@code RequestDetails} 创建器
 * <p>
 * 一般说来，RequestDetails是对一个HTTP请求的简要描述，类型一般为 {@link String}。
 *
 * @author 应卓
 *
 * @see #SIMPLE_DESCRIPTION
 * @see #SNAPSHOT
 * @see #SPRING_SECURITY_DEFAULT
 * @see #AUTHENTICATION_DETAILS_OBJ
 *
 * @since 1.0.4
 */
@FunctionalInterface
public interface RequestDetailsProvider {

    /**
     * 无动作实现 (默认)
     */
    public static final RequestDetailsProvider ALWAYS_NULL = (request, token) -> null;

    /**
     * SpringSecurity默认实现
     */
    public static final RequestDetailsProvider SPRING_SECURITY_DEFAULT = (request,
            token) -> new WebAuthenticationDetailsSource().buildDetails(request).toString();

    /**
     * HTTP(s)快照
     */
    public static final RequestDetailsProvider SNAPSHOT = (request, token) -> HttpRequestSnapshot.of(request)
            .toString();

    /**
     * HTTP信息简要描述
     */
    public static final RequestDetailsProvider SIMPLE_DESCRIPTION = (request, token) -> new ServletWebRequest(request)
            .getDescription(true);

    /**
     * 对象类型
     */
    public static final RequestDetailsProvider AUTHENTICATION_DETAILS_OBJ = AuthenticationDetailsImpl::new;

    /**
     * 创建Details
     *
     * @param request
     *            HTTP请求
     * @param token
     *            令牌
     *
     * @return details或{@code null}
     */
    @Nullable
    public Object getDetails(HttpServletRequest request, @Nullable Token token);

    /**
     * 创建Details
     *
     * @param request
     *            HTTP请求
     *
     * @return details或{@code null}
     */
    @Nullable
    public default Object getDetails(HttpServletRequest request) {
        return getDetails(request, null);
    }

}
