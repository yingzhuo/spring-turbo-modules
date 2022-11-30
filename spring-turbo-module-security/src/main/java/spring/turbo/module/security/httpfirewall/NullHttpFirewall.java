/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.httpfirewall;

import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import spring.turbo.lang.Singleton;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 1.3.1
 */
@Singleton
public final class NullHttpFirewall extends DefaultHttpFirewall implements HttpFirewall {

    /**
     * 私有构造方法
     */
    private NullHttpFirewall() {
        super.setAllowUrlEncodedSlash(true);
    }

    /**
     * 获取单例
     *
     * @return 单例实例
     */
    public static NullHttpFirewall getInstance() {
        return SyncAvoid.INSTANCE;
    }

    // 延迟加载
    private static class SyncAvoid {
        public static final NullHttpFirewall INSTANCE = new NullHttpFirewall();
    }

}
