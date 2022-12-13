/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.user;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import spring.turbo.io.PropertiesFormat;
import spring.turbo.io.ResourceOption;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.Asserts;

/**
 * {@link InMemoryUserDetailsManager} 工厂
 *
 * @author 应卓
 * @see FactoryBean
 * @since 2.0.4
 */
public class ResourceOptionUserDetailsManagerFactoryBean implements FactoryBean<InMemoryUserDetailsManager> {

    private final ResourceOption resourceOption;

    public ResourceOptionUserDetailsManagerFactoryBean(ResourceOption resourceOption) {
        Asserts.notNull(resourceOption);
        this.resourceOption = resourceOption;
        this.enforcePresent();
    }

    public ResourceOptionUserDetailsManagerFactoryBean(String... resourceLocations) {
        Asserts.notNull(resourceLocations);
        Asserts.noNullElements(resourceLocations);
        Asserts.isTrue(resourceLocations.length > 0);
        this.resourceOption = ResourceOptions.builder()
                .add(resourceLocations)
                .build();
        this.enforcePresent();
    }

    private void enforcePresent() {
        if (this.resourceOption.isAbsent()) {
            throw new BeanCreationException("resource option is absent");
        }
    }

    @Override
    public InMemoryUserDetailsManager getObject() {
        try {
            return new InMemoryUserDetailsManager(this.resourceOption.toProperties(PropertiesFormat.PROPERTIES));
        } catch (Exception e) {
            throw new BeanCreationException(e.getMessage());
        }
    }

    @Override
    public Class<?> getObjectType() {
        return InMemoryUserDetailsManager.class;
    }

}
