package spring.turbo.module.security.filter;

import spring.turbo.module.security.token.BearerTokenResolver;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class BearerTokenAuthenticationFilter extends TokenAuthenticationFilter {

    /**
     * 默认构造方法
     */
    public BearerTokenAuthenticationFilter() {
        super.setTokenResolver(new BearerTokenResolver());
    }

}
