package spring.turbo.module.security.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import spring.turbo.module.security.authentication.UserDetailsFinder;
import spring.turbo.module.security.authentication.UserDetailsServiceUserDetailsFinder;
import spring.turbo.module.security.exception.SecurityExceptionHandler;
import spring.turbo.module.security.exception.SecurityExceptionHandlerImpl;

/**
 * @author 应卓
 * @since 3.3.1
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SpringSecurityAutoConfiguration {

    /**
     * 默认构造方法
     */
    public SpringSecurityAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpFirewall httpFirewall() {
        var bean = new StrictHttpFirewall();
        bean.setAllowSemicolon(true);
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({UserDetailsService.class, PasswordEncoder.class})
    public UserDetailsFinder userDetailsFinder(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder
    ) {
        return new UserDetailsServiceUserDetailsFinder(userDetailsService, passwordEncoder);
    }

}
