/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jackson;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import spring.turbo.SpringTurboVersion;
import spring.turbo.module.security.token.BasicToken;
import spring.turbo.module.security.token.StringToken;

/**
 * @author 应卓
 *
 * @since 2.0.3
 */
public class SecurityModule extends SimpleModule {

    /**
     * 默认构造方法
     */
    public SecurityModule() {
        super(SecurityModule.class.getName(), VersionUtil.parseVersion(SpringTurboVersion.CURRENT,
                "com.github.yingzhuo", "spring-turbo-module-security"));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(StringToken.class, StringTokenMixin.class);
        context.setMixInAnnotations(BasicToken.class, BasicTokenMixin.class);
    }

}
