package spring.turbo.module.security.filter.factory;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.Assert;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.module.security.exception.SecurityExceptionHandler;
import spring.turbo.module.security.filter.JwtTokenAuthenticationFilter;
import spring.turbo.module.security.token.BearerTokenResolver;
import spring.turbo.module.security.token.TokenResolver;
import spring.turbo.module.security.token.blacklist.TokenBlacklistManager;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class JwtTokenAuthenticationFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>>, InitializingBean {

    private FilterConfiguration.Position position = FilterConfiguration.Position.AFTER;
    private Class<? extends Filter> positionInChain = org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class;
    private TokenResolver tokenResolver = new BearerTokenResolver();
    private @Nullable TokenToUserConverter tokenToUserConverter;
    private @Nullable ApplicationEventPublisher applicationEventPublisher;
    private @Nullable AuthenticationEntryPoint authenticationEntryPoint;
    private @Nullable RememberMeServices rememberMeServices;
    private @Nullable TokenBlacklistManager tokenBlacklistManager;

    /**
     * 默认构造方法
     */
    public JwtTokenAuthenticationFilterFactoryBean() {
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public FilterConfiguration<Filter> getObject() {
        Assert.notNull(tokenToUserConverter, "tokenToUserConverter is required");

        var filter = new JwtTokenAuthenticationFilter();
        filter.setTokenResolver(tokenResolver);
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
        Assert.notNull(tokenToUserConverter, "tokenToUserConverter is required");
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
