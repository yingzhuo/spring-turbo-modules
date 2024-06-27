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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import spring.turbo.module.security.DefaultFilterConfiguration;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.authentication.RequestDetailsProvider;
import spring.turbo.module.security.authentication.UserDetailsServiceUserDetailsFinder;
import spring.turbo.module.security.exception.SecurityExceptionHandlerImpl;
import spring.turbo.module.security.filter.BasicAuthenticationFilter;
import spring.turbo.module.security.token.BasicTokenResolver;
import spring.turbo.module.security.token.TokenResolver;
import spring.turbo.module.security.token.blacklist.TokenBlacklistManager;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class BasicAuthenticationFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>>, InitializingBean {

    private FilterConfiguration.Position position = FilterConfiguration.Position.REPLACE;
    private Class<? extends Filter> positionInChain = org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class;
    private TokenResolver tokenResolver = new BasicTokenResolver();
    private RequestDetailsProvider requestDetailsProvider = RequestDetailsProvider.SIMPLE_DESCRIPTION;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher applicationEventPublisher;
    private AuthenticationEntryPoint authenticationEntryPoint = new SecurityExceptionHandlerImpl();
    private RememberMeServices rememberMeServices;
    private TokenBlacklistManager tokenBlacklistManager;

    /**
     * 默认构造方法
     */
    public BasicAuthenticationFilterFactoryBean() {
        super();
    }

    @Override
    public FilterConfiguration<Filter> getObject() {
        var filter = new BasicAuthenticationFilter();
        filter.setTokenResolver(tokenResolver);
        filter.setRequestDetailsProvider(requestDetailsProvider);
        filter.setUserDetailsFinder(new UserDetailsServiceUserDetailsFinder(userDetailsService, passwordEncoder));
        filter.setApplicationEventPublisher(applicationEventPublisher);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setRememberMeServices(rememberMeServices);
        filter.setTokenBlacklistManager(tokenBlacklistManager);
        return new DefaultFilterConfiguration(filter, positionInChain, position);
    }

    @Override
    public Class<?> getObjectType() {
        return FilterConfiguration.class;
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.notNull(userDetailsService, "userDetailsService is required");
        Asserts.notNull(passwordEncoder, "passwordEncoder is required");
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

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    public void setTokenBlacklistManager(TokenBlacklistManager tokenBlacklistManager) {
        this.tokenBlacklistManager = tokenBlacklistManager;
    }

}
