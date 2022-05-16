/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
@NonNullApi
@NonNullFields
package spring.turbo.module.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import spring.turbo.module.zookeeper.client.LeaderLatchFactory;
import spring.turbo.module.zookeeper.client.ZookeeperClientFactory;
import spring.turbo.module.zookeeper.configuration.ZookeeperProperties;

/**
 * @author 应卓
 * @since 1.0.15
 */
@EnableConfigurationProperties(ZookeeperProperties.class)
@ConditionalOnProperty(prefix = "springturbo.zookeeper", name = "enabled", havingValue = "true", matchIfMissing = true)
class SpringBootAutoConfiguration {

    @Bean
    public ZookeeperClientFactory zookeeperClientFactory(ZookeeperProperties properties) {
        return new ZookeeperClientFactory(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "springturbo.zookeeper.leader-election", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LeaderLatchFactory leaderLatchFactory(ZookeeperProperties properties, CuratorFramework zkClient) {
        return new LeaderLatchFactory(properties, zkClient);
    }

}
