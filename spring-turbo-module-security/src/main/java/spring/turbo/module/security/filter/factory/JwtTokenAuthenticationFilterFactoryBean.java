/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter.factory;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.authentication.RequestDetailsProvider;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.module.security.exception.SecurityExceptionHandler;
import spring.turbo.module.security.filter.JwtTokenAuthenticationFilter;
import spring.turbo.module.security.token.BearerTokenResolver;
import spring.turbo.module.security.token.TokenResolver;
import spring.turbo.module.security.token.blacklist.TokenBlacklistManager;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class JwtTokenAuthenticationFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>>, InitializingBean {

    private FilterConfiguration.Position position = FilterConfiguration.Position.AFTER;
    private Class<? extends Filter> positionInChain = org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class;
    private TokenResolver tokenResolver = new BearerTokenResolver();
    private RequestDetailsProvider requestDetailsProvider = RequestDetailsProvider.SPRING_SECURITY_DEFAULT;
    private @Nullable TokenToUserConverter tokenToUserConverter;
    private @Nullable ApplicationEventPublisher applicationEventPublisher;
    private @Nullable AuthenticationEntryPoint authenticationEntryPoint;
    private @Nullable RememberMeServices rememberMeServices;
    private @Nullable TokenBlacklistManager tokenBlacklistManager;

    /**
     * 默认构造方法
     */
    public JwtTokenAuthenticationFilterFactoryBean() {
        super();
    }

    @Override
    public FilterConfiguration<Filter> getObject() {
        Asserts.notNull(tokenToUserConverter);

        var filter = new JwtTokenAuthenticationFilter();
        filter.setTokenResolver(tokenResolver);
        filter.setRequestDetailsProvider(requestDetailsProvider);
        filter.setTokenToUserConverter(tokenToUserConverter);
        filter.setApplicationEventPublisher(applicationEventPublisher);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setRememberMeServices(rememberMeServices);
        filter.setTokenBlacklistManager(tokenBlacklistManager);
        return new FilterConfiguration.Default(filter, positionInChain, position);
    }

    @Override
    public Class<?> getObjectType() {
        return FilterConfiguration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.notNull(tokenToUserConverter, "tokenToUserConverter is required");
    }

    public void setPosition(FilterConfiguration.Position position) {
        this.position = position;
    }

    public void setPositionInChain(Class<? extends Filter> positionInChain) {
        this.positionInChain = positionInChain;
    }

    public void setTokenResolver(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    public void setRequestDetailsProvider(RequestDetailsProvider requestDetailsProvider) {
        this.requestDetailsProvider = requestDetailsProvider;
    }

    public void setTokenToUserConverter(TokenToUserConverter tokenToUserConverter) {
        this.tokenToUserConverter = tokenToUserConverter;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setSecurityExceptionHandler(SecurityExceptionHandler securityExceptionHandler) {
        this.setAuthenticationEntryPoint((AuthenticationEntryPoint) securityExceptionHandler);
    }

    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setTokenBlacklistManager(TokenBlacklistManager tokenBlacklistManager) {
        this.tokenBlacklistManager = tokenBlacklistManager;
    }

}
