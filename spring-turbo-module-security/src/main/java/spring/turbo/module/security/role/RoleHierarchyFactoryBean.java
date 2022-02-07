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
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import spring.turbo.io.ResourceOption;
import spring.turbo.io.ResourceOptions;
import spring.turbo.util.Asserts;

import java.nio.charset.Charset;

import static spring.turbo.util.CharsetPool.UTF_8;

/**
 * {@link RoleHierarchy} 工厂，本来使用文件配置角色之间的继承关系。
 *
 * @author 应卓
 * @since 1.0.0
 */
public final class RoleHierarchyFactoryBean implements FactoryBean<RoleHierarchy> {

    @Nullable
    private ResourceOption text;

    private Charset charset = UTF_8;

    /**
     * 构造方法
     */
    public RoleHierarchyFactoryBean() {
        super();
    }

    @Override
    public RoleHierarchy getObject() {
        Asserts.state(text != null && text.isPresent());

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
        Asserts.notNull(textResource);
        this.text = ResourceOptions.builder()
                .add(textResource)
                .build();
    }

    public void setCharset(Charset charset) {
        Asserts.notNull(charset);
        this.charset = charset;
    }

}
