package spring.turbo.module.security.token;

import org.springframework.http.HttpHeaders;

/**
 * HTTP Bearer 令牌解析器
 *
 * @author 应卓
 * @see HeaderTokenResolver
 * @see BasicTokenResolver
 * @see HttpHeaders#AUTHORIZATION
 * @since 1.0.5
 */
public final class BearerTokenResolver extends HeaderTokenResolver {

    private static final String PREFIX = "Bearer ";

    /**
     * 构造方法
     */
    public BearerTokenResolver() {
        super(HttpHeaders.AUTHORIZATION, PREFIX);
    }

}
