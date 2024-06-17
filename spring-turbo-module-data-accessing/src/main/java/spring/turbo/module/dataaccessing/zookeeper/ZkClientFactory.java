/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dataaccessing.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import spring.turbo.module.dataaccessing.zookeeper.properties.ZookeeperProps;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.15
 */
public class ZkClientFactory implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {

    private final ZookeeperProps zkProps;

    @Nullable
    private CuratorFramework zkCli;

    public ZkClientFactory(ZookeeperProps zkProps) {
        Asserts.notNull(zkProps);
        this.zkProps = zkProps;
    }

    @Nullable
    @Override
    public CuratorFramework getObject() {
        return zkCli;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.zkCli = CuratorFrameworkFactory.builder().connectString(zkProps.getConnectString())
                .namespace(zkProps.getNamespace())
                .retryPolicy(new ExponentialBackoffRetry(zkProps.getBackoffRetryPolicy().getBaseSleepTime(),
                        zkProps.getBackoffRetryPolicy().getMaxRetries()))
                .build();
        this.zkCli.start();
        this.zkCli.getZookeeperClient().blockUntilConnectedOrTimedOut();
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() {
        CloseableUtils.closeQuietly(zkCli);
    }

}
