/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.zookeeper.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import spring.turbo.module.zookeeper.configuration.ZookeeperProperties;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.15
 */
public class ZookeeperClientFactory implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {

    private final ZookeeperProperties zookeeperProperties;

    @Nullable
    private CuratorFramework zookeeperClient;

    public ZookeeperClientFactory(ZookeeperProperties zookeeperProperties) {
        Asserts.notNull(zookeeperProperties);
        this.zookeeperProperties = zookeeperProperties;
    }

    @Nullable
    @Override
    public CuratorFramework getObject() {
        return zookeeperClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final RetryPolicy retryPolicy = new ExponentialBackoffRetry(
                zookeeperProperties.getBackoffRetryPolicy().getBaseSleepTime(),
                zookeeperProperties.getBackoffRetryPolicy().getMaxRetries()
        );
        this.zookeeperClient = CuratorFrameworkFactory.newClient(zookeeperProperties.getConnectString(), retryPolicy);
        this.zookeeperClient.start();
        this.zookeeperClient.getZookeeperClient().blockUntilConnectedOrTimedOut();
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
        CloseableUtils.closeQuietly(zookeeperClient);
    }

}
