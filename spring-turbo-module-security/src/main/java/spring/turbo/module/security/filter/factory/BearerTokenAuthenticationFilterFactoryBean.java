package spring.turbo.module.security.filter.factory;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.authentication.RequestDetailsProvider;
import spring.turbo.module.security.authentication.TokenToUserConverter;
import spring.turbo.module.security.filter.BearerTokenAuthenticationFilter;
import spring.turbo.module.security.token.BearerTokenResolver;
import spring.turbo.module.security.token.TokenResolver;
import spring.turbo.module.security.token.blacklist.TokenBlacklistManager;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class BearerTokenAuthenticationFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>> {

    private FilterConfiguration.Position position = FilterConfiguration.Position.BEFORE;
    private Class<? extends Filter> positionInChain = org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class;
    private TokenResolver tokenResolver = new BearerTokenResolver();
    private RequestDetailsProvider requestDetailsProvider = RequestDetailsProvider.SPRING_SECURITY_DEFAULT;
    private @Nullable TokenToUserConverter tokenToUserConverter;
    private @Nullable ApplicationEventPublisher applicationEventPublisher;
    private @Nullable AuthenticationEntryPoint authenticationEntryPoint;
    private @Nullable RememberMeServices rememberMeServices;
    private @Nullable TokenBlacklistManager tokenBlacklistManager;

    @Override
    public FilterConfiguration<Filter> getObject() {
        Asserts.notNull(tokenToUserConverter);

        var filter = new BearerTokenAuthenticationFilter();
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

    public void setTokenToUserConverter(@Nullable TokenToUserConverter tokenToUserConverter) {
        this.tokenToUserConverter = tokenToUserConverter;
    }

    public void setApplicationEventPublisher(@Nullable ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setAuthenticationEntryPoint(@Nullable AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setRememberMeServices(@Nullable RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setTokenBlacklistManager(@Nullable TokenBlacklistManager tokenBlacklistManager) {
        this.tokenBlacklistManager = tokenBlacklistManager;
    }

}
