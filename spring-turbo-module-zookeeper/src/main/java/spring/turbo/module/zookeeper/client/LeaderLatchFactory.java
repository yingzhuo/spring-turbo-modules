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
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import spring.turbo.module.zookeeper.configuration.ZookeeperProperties;
import spring.turbo.util.Asserts;

import java.util.UUID;

/**
 * @author 应卓
 * @since 1.0.15
 */
public class LeaderLatchFactory implements FactoryBean<LeaderLatch>, InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(LeaderLatchFactory.class);

    private final ZookeeperProperties zkProps;
    private final CuratorFramework zkCli;

    @Nullable
    private LeaderLatch leaderLatch;

    public LeaderLatchFactory(ZookeeperProperties zkProps, CuratorFramework zkCli) {
        Asserts.notNull(zkProps);
        Asserts.notNull(zkCli);
        this.zkProps = zkProps;
        this.zkCli = zkCli;
    }

    @Nullable
    @Override
    public LeaderLatch getObject() {
        return this.leaderLatch;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return LeaderLatch.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String thisNodeId = zkProps.getLeaderElection().getNodeId();
        if (thisNodeId == null) {
            thisNodeId = UUID.randomUUID().toString();
            log.debug("node-id: {}", thisNodeId);
        }

        this.leaderLatch = new LeaderLatch(
                this.zkCli,
                this.zkProps.getLeaderElection().getZkPath(),
                thisNodeId
        );

        this.leaderLatch.start();
    }

    @Override
    public void destroy() {
        CloseableUtils.closeQuietly(this.leaderLatch);
    }

}
