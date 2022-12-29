/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.usermanager;

import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import spring.turbo.io.PropertiesFormat;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.Asserts;

/**
 * {@link InMemoryUserDetailsManager} 工厂
 *
 * @author 应卓
 * @since 2.0.4
 */
public class InMemoryUserDetailsManagerFactoryBean implements SmartFactoryBean<InMemoryUserDetailsManager>, ResourceLoaderAware {

    @Nullable
    private ResourceLoader resourceLoader;

    @Nullable
    private String resourceLocation;

    @Nullable
    private PropertiesFormat propertiesFormat = PropertiesFormat.PROPERTIES;

    private boolean lazyInit = false;

    private boolean prototype = false;

    /**
     * 默认构造方法
     */
    public InMemoryUserDetailsManagerFactoryBean() {
        super();
    }

    @Override
    public InMemoryUserDetailsManager getObject() throws FactoryBeanNotInitializedException {
        Asserts.notNull(resourceLoader);
        Asserts.notNull(propertiesFormat);
        Asserts.hasText(resourceLocation);

        try {
            final var props = ResourceOptions.builder()
                    .add(resourceLocation)
                    .build()
                    .toProperties(propertiesFormat);
            return new InMemoryUserDetailsManager(props);
        } catch (Exception e) {
            throw new FactoryBeanNotInitializedException(e.getMessage());
        }

    }

    @Override
    public Class<?> getObjectType() {
        return InMemoryUserDetailsManager.class;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    @Override
    public boolean isEagerInit() {
        return !this.lazyInit;
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

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

}
