package spring.turbo.module.dataaccessing.zookeeper.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.15
 */
@Data
@ConfigurationProperties(prefix = "springturbo.zookeeper")
public class ZookeeperProps implements InitializingBean, Serializable {

    private boolean enabled = false;
    private String connectString;
    private String namespace;
    private BackoffRetryPolicy backoffRetryPolicy = new BackoffRetryPolicy();
    private LeaderElection leaderElection = new LeaderElection();

    @Override
    public void afterPropertiesSet() {
        if (leaderElection.enabled) {
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Data
    public static class BackoffRetryPolicy implements Serializable {
        private int baseSleepTime = 1000;
        private int maxRetries = 29;

        public int getBaseSleepTime() {
            return baseSleepTime;
        }

        public void setBaseSleepTime(int baseSleepTime) {
            this.baseSleepTime = baseSleepTime;
        }

        public int getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Data
    public static class LeaderElection implements Serializable {

        private boolean enabled = true;

        private String zkPath = "/leader-election";

        @Nullable
        private String nodeId;
    }

}
