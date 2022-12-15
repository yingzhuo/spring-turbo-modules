/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.usermanager;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import spring.turbo.io.PropertiesFormat;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 2.0.4
 */
public class InMemoryUserDetailsManagerFactoryBean implements FactoryBean<InMemoryUserDetailsManager>, ResourceLoaderAware {

    @Nullable
    private ResourceLoader resourceLoader;

    @Nullable
    private String resourceLocation;

    @Nullable
    private PropertiesFormat propertiesFormat = PropertiesFormat.PROPERTIES;

    /**
     * 构造方法
     */
    public InMemoryUserDetailsManagerFactoryBean() {
        super();
    }

    @Override
    public InMemoryUserDetailsManager getObject() {
        Asserts.notNull(resourceLoader);
        Asserts.notNull(propertiesFormat);
        Asserts.hasText(resourceLocation);
        final var props = ResourceOptions.builder()
                .add(resourceLocation)
                .build()
                .toProperties(propertiesFormat);
        return new InMemoryUserDetailsManager(props);
    }

    @Override
    public Class<?> getObjectType() {
        return InMemoryUserDetailsManager.class;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public void setPropertiesFormat(PropertiesFormat propertiesFormat) {
        this.propertiesFormat = propertiesFormat;
    }

}
