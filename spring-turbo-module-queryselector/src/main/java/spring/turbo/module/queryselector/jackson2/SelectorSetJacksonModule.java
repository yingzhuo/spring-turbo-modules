/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.jackson2;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import spring.turbo.SpringTurboVersion;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorSet;

/**
 * @author 应卓
 *
 * @since 2.0.1
 */
public class SelectorSetJacksonModule extends SimpleModule {

    /**
     * 默认构造方法
     */
    public SelectorSetJacksonModule() {
        super(SelectorSetJacksonModule.class.getName(), VersionUtil.parseVersion(SpringTurboVersion.CURRENT,
                "com.github.yingzhuo", "spring-turbo-module-queryselector"));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(Selector.class, SelectorMixin.class);
        context.setMixInAnnotations(SelectorSet.class, SelectorSetMixin.class);
    }

}
