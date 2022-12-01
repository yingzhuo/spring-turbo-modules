/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.locale;

import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import spring.turbo.lang.Singleton;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author 应卓
 * @since 2.0.1
 */
@Singleton
public final class SystemDefaultLocaleResolver extends FixedLocaleResolver {

    public static SystemDefaultLocaleResolver getInstance( ) {
        return SyncAvoid.INSTANCE;
    }

    /**
     * 私有构造方法
     */
    private SystemDefaultLocaleResolver() {
        super(Locale.getDefault(), TimeZone.getDefault());
    }

    // 延迟加载
    private static class SyncAvoid {
        public static final SystemDefaultLocaleResolver INSTANCE = new SystemDefaultLocaleResolver();
    }

}
