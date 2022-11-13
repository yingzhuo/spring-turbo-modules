/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.role;

import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import spring.turbo.io.ResourceUtils;
import spring.turbo.util.Asserts;
import spring.turbo.util.CharsetPool;

import java.nio.charset.Charset;

/**
 * {@link RoleHierarchy} 工厂，本来使用文件配置角色之间的继承关系。
 *
 * @author 应卓
 * @see RoleHierarchy
 * @see RoleHierarchyImpl
 * @see ResourceUtils
 * @since 1.0.0
 */
public final class RoleHierarchyFactoryBean implements SmartFactoryBean<RoleHierarchy> {

    private final String hierarchy;

    /**
     * 构造方法
     *
     * @param roleHierarchyStringRepresentation 配置
     */
    public RoleHierarchyFactoryBean(String roleHierarchyStringRepresentation) {
        Asserts.hasText(roleHierarchyStringRepresentation);
        this.hierarchy = roleHierarchyStringRepresentation;
    }

    /**
     * 构造方法
     *
     * @param roleHierarchyStringRepresentation 配置文件
     */
    public RoleHierarchyFactoryBean(Resource roleHierarchyStringRepresentation) {
        this(roleHierarchyStringRepresentation, CharsetPool.UTF_8);
    }

    /**
     * 构造方法
     *
     * @param roleHierarchyStringRepresentation 配置文件
     * @param charset                           配置文件字符集
     */
    public RoleHierarchyFactoryBean(Resource roleHierarchyStringRepresentation, Charset charset) {
        Asserts.notNull(roleHierarchyStringRepresentation);
        Asserts.notNull(charset);
        String configString = ResourceUtils.toString(roleHierarchyStringRepresentation, charset);
        Asserts.hasText(configString);
        this.hierarchy = configString;
    }

    @Override
    public RoleHierarchy getObject() {
        final RoleHierarchyImpl bean = new RoleHierarchyImpl();
        bean.setHierarchy(hierarchy);
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

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isEagerInit() {
        return true;
    }
}
