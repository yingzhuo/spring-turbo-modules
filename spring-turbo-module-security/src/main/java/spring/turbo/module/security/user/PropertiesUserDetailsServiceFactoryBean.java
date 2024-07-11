package spring.turbo.module.security.user;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * {@link UserDetailsService} 配置工具。这个类从 properties 文件中加载数据。
 * <p>
 * 格式如下:
 * <pre>
 * bob={noop}bob,enabled,ROLE_USER,ROLE_ADMIN
 * </pre>
 *
 * @author 应卓
 * @see InMemoryUserDetailsManager
 * @since 3.3.1
 */
public class PropertiesUserDetailsServiceFactoryBean implements FactoryBean<UserDetailsService>, ResourceLoaderAware, InitializingBean {

    private final Properties properties = new Properties();
    private String location;
    private ResourceLoader resourceLoader;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetailsService getObject() {
        return new InMemoryUserDetailsManager(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return UserDetailsService.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.hasText(location)) {
            var resource = this.resourceLoader.getResource(this.location);
            var filename = resource.getFilename();

            try (var input = resource.getInputStream()) {
                if (StringUtils.endsWithIgnoreCase(filename, ".xml")) {
                    this.properties.loadFromXML(input);
                } else {
                    this.properties.load(input);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
