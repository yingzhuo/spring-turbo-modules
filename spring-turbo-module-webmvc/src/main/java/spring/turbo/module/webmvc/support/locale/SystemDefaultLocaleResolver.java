/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.support.locale;

import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 2.0.1
 */
public final class SystemDefaultLocaleResolver extends FixedLocaleResolver {

    /**
     * 私有构造方法
     */
    private SystemDefaultLocaleResolver() {
        super(Locale.getDefault(), TimeZone.getDefault());
    }

    /**
     * 获取实例
     *
     * @return 实例 (单例)
     */
    public static SystemDefaultLocaleResolver getInstance() {
        return SyncAvoid.INSTANCE;
    }

    // 延迟加载
    private static class SyncAvoid {
        public static final SystemDefaultLocaleResolver INSTANCE = new SystemDefaultLocaleResolver();
    }

}
