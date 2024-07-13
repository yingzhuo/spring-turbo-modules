package spring.turbo.module.dataaccessing.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import spring.turbo.core.SpringUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 可重入分布式锁
 *
 * @author 应卓
 * @see #newInstance(String)
 * @since 1.0.15
 */
public final class ZkLock implements Serializable {

    private final InterProcessMutex mutex;

    /**
     * 私有构造方法
     *
     * @param zkClient 客户端实例
     * @param zkPath   锁专用Path
     */
    private ZkLock(CuratorFramework zkClient, String zkPath) {
        this.mutex = new InterProcessMutex(zkClient, zkPath);
    }

    public static ZkLock newInstance(String zkPath) {
        final CuratorFramework zkClient = SpringUtils.getRequiredBean(CuratorFramework.class);
        return new ZkLock(zkClient, zkPath);
    }

    public boolean lock(long ttl, TimeUnit ttlUnit) {
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
