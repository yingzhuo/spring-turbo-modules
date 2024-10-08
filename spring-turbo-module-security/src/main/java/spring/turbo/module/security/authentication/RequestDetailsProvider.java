package spring.turbo.module.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import spring.turbo.module.security.token.Token;

/**
 * {@code RequestDetails} 创建器
 * <p>
 * 一般说来，RequestDetails是对一个HTTP请求的简要描述，类型一般为 {@link String}。
 *
 * @author 应卓
 * @since 1.0.4
 */
@FunctionalInterface
@Deprecated(forRemoval = true, since = "3.3.1")
public interface RequestDetailsProvider {

    /**
     * SpringSecurity默认实现
     */
    public static final RequestDetailsProvider SPRING_SECURITY_DEFAULT = (request,
                                                                          token) -> new WebAuthenticationDetailsSource().buildDetails(request).toString();

    /**
     * NULL
     */
    public static final RequestDetailsProvider NULL = (request, token) -> null;

    /**
     * 创建Details
     *
     * @param request HTTP请求
     * @param token   令牌
     * @return details或{@code null}
     */
    @Nullable
    public Object getDetails(HttpServletRequest request, @Nullable Token token);

    /**
     * 创建Details
     *
     * @param request HTTP请求
     * @return details或{@code null}
     */
    @Nullable
    public default Object getDetails(HttpServletRequest request) {
        return getDetails(request, null);
    }

}
