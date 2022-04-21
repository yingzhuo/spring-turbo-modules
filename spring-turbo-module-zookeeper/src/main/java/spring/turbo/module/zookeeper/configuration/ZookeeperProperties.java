/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.zookeeper.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.15
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "springturbo.zookeeper")
public class ZookeeperProperties implements Serializable {

    private String connectString = "localhost:2181";
    private BackoffRetryPolicy backoffRetryPolicy = new BackoffRetryPolicy();
    private LeaderElection leaderElection = new LeaderElection();

    @Getter
    @Setter
    public static class BackoffRetryPolicy implements Serializable {
        private int baseSleepTime = 1000;
        private int maxRetries = 29;
    }

    @Getter
    @Setter
    public static class LeaderElection implements Serializable {
        private boolean enabled = false;
        private String zkPath = "/leader-election";
        private String nodeId;
    }

}
