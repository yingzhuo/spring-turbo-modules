/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import spring.turbo.SpringTurboVersion;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorSet;

/**
 * @author 应卓
 * @since 1.3.0
 */
public class SelectorSetJacksonModule extends SimpleModule {

    public static final Version MODULE_VERSION = VersionUtil.parseVersion(
            SpringTurboVersion.VERSION,
            "com.github.yingzhuo",
            "spring-turbo-module-queryselector"
    );

    public SelectorSetJacksonModule() {
        super(SelectorSetJacksonModule.class.getName(), MODULE_VERSION);
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(Selector.class, SelectorMixin.class);
        context.setMixInAnnotations(SelectorSet.class, SelectorSetMixin.class);
    }

}
