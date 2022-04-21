/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.zookeeper.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import spring.turbo.core.SpringUtils;
import spring.turbo.util.Asserts;

import java.util.concurrent.TimeUnit;

/**
 * 可重入分布式锁
 *
 * @author 应卓
 * @see #newInstance(String)
 * @since 1.0.15
 */
public final class Lock {

    private final InterProcessMutex mutex;

    public static Lock newInstance(String zkPath) {
        final CuratorFramework zkClient = SpringUtils.getRequiredBean(CuratorFramework.class);
        Asserts.notNull(zkClient);
        return new Lock(zkClient, zkPath);
    }

    private Lock(CuratorFramework zkClient, String zkPath) {
        this.mutex = new InterProcessMutex(zkClient, zkPath);
    }

    public boolean lock(long ttl, TimeUnit ttlUnit) {
        Asserts.isTrue(ttl > 0);
        Asserts.notNull(ttlUnit);

        try {
            return mutex.acquire(ttl, ttlUnit);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean release() {
        try {
            mutex.release();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
