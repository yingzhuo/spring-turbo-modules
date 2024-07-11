package spring.turbo.module.security.user;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
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
 * @see PropertiesUserDetailsService
 * @since 3.3.1
 */
public class PropertiesUserDetailsServiceFactoryBean implements FactoryBean<PropertiesUserDetailsService>, ResourceLoaderAware, InitializingBean {

    private final Properties properties = new Properties();

    private ResourceLoader resourceLoader = new DefaultResourceLoader(ClassUtils.getDefaultClassLoader());

    @Nullable
    private String location;

    /**
     * 默认构造方法
     */
    public PropertiesUserDetailsServiceFactoryBean() {
        this.location = null;
    }

    /**
     * 构造方法
     *
     * @param location properties 文件位置
     * @see ResourceLoader
     * @see ResourceLoaderAware
     */
    public PropertiesUserDetailsServiceFactoryBean(String location) {
        setLocation(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesUserDetailsService getObject() {
        return new PropertiesUserDetailsService(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return PropertiesUserDetailsService.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(location, () -> "location is required");

        var resource = resourceLoader.getResource(location);
        var filename = resource.getFilename();

        try (var input = resource.getInputStream()) {
            if (StringUtils.endsWithIgnoreCase(filename, ".xml")) {
                properties.loadFromXML(input);
            } else {
                properties.load(input);
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
