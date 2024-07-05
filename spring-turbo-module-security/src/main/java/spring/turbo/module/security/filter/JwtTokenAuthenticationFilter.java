package spring.turbo.module.security.filter;

import spring.turbo.module.security.authentication.MapTokenToUserConverter;
import spring.turbo.module.security.token.BearerTokenResolver;

/**
 * @author 应卓
 * @see MapTokenToUserConverter
 * @see TokenAuthenticationFilter
 * @see spring.turbo.module.security.filter.factory.JwtTokenAuthenticationFilterFactoryBean
 * @since 1.0.1
 */
public final class JwtTokenAuthenticationFilter extends TokenAuthenticationFilter {

    /**
     * 默认构造方法
     */
    public JwtTokenAuthenticationFilter() {
        super.setTokenResolver(new BearerTokenResolver());
    }

}
