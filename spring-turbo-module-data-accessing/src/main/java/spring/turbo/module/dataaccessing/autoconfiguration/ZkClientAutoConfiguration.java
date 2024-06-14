/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dataaccessing.autoconfiguration;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.dataaccessing.zookeeper.LeaderLatchFactory;
import spring.turbo.module.dataaccessing.zookeeper.ZkClientFactory;
import spring.turbo.module.dataaccessing.zookeeper.properties.ZookeeperProps;

/**
 * @author 应卓
 *
 * @since 1.3.0
 */
@AutoConfiguration
@EnableConfigurationProperties(ZookeeperProps.class)
@ConditionalOnClass(name = "org.apache.curator.framework.CuratorFramework")
@ConditionalOnProperty(prefix = "springturbo.zookeeper", name = "enabled", havingValue = "true", matchIfMissing = false)
public class ZkClientAutoConfiguration {

    @Bean
    public ZkClientFactory zookeeperClientFactory(ZookeeperProps properties) {
        return new ZkClientFactory(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "springturbo.zookeeper.leader-election", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LeaderLatchFactory leaderLatchFactory(ZookeeperProps properties, CuratorFramework zkClient) {
        return new LeaderLatchFactory(properties, zkClient);
    }

}
