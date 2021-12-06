/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.role;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import spring.turbo.io.ResourceOption;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.Asserts;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class RoleHierarchyFactoryBean implements FactoryBean<RoleHierarchy>, InitializingBean {

    private ResourceOption text;
    private Charset charset = UTF_8;

    public RoleHierarchyFactoryBean() {
        super();
    }

    @Override
    public RoleHierarchy getObject() {
        final RoleHierarchyImpl bean = new RoleHierarchyImpl();
        bean.setHierarchy(text.toString(charset));
        return bean;
    }

    @Override
    public Class<?> getObjectType() {
        return RoleHierarchy.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setText(Resource textResource) {
        this.text = ResourceOptions.builder()
                .add(textResource)
                .build();
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.state(text != null && text.isPresent());
        Asserts.state(charset != null);
    }

}
