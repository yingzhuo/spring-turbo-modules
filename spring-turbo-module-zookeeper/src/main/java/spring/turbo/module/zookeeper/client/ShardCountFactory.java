/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.15
 */
public class ShardCountFactory implements FactoryBean<SharedCount>, InitializingBean, DisposableBean {

    private final SharedCount count;

    public ShardCountFactory(CuratorFramework zkCli, String path, int seedValue) {
        Asserts.notNull(zkCli);
        Asserts.notNull(path);
        this.count = new SharedCount(zkCli, path, seedValue);
    }

    @Nullable
    @Override
    public SharedCount getObject() {
        return this.count;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return SharedCount.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.count.start();
    }

    @Override
    public void destroy() throws Exception {
        CloseableUtils.closeQuietly(this.count);
    }

}
