/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.zookeeper.util;

import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.springframework.lang.Nullable;
import spring.turbo.core.SpringUtils;

/**
 * @author 应卓
 * @since 1.0.15
 */
public final class LeadershipUtils {

    /**
     * 私有构造方法
     */
    private LeadershipUtils() {
        super();
    }

    /**
     * 判断此节点是否有集群的领导权
     *
     * @return 返回{@code true}时表示有领导权
     */
    public static boolean hasLeadership() {
        return SpringUtils.getRequiredBean(LeaderLatch.class)
                .hasLeadership();
    }

    @Nullable
    public static String getLeaderId() {
        final LeaderLatch leaderLatch = SpringUtils.getRequiredBean(LeaderLatch.class);
        try {
            return leaderLatch.getLeader().getId();
        } catch (Exception e) {
            return null;
        }
    }

}
