/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jackson;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import spring.turbo.webmvc.token.BasicToken;
import spring.turbo.webmvc.token.StringToken;

/**
 * @author 应卓
 * @since 1.2.3
 */
public class SpringTurboModulesSecurityCoreModule extends SimpleModule {

    public SpringTurboModulesSecurityCoreModule() {
        super(SpringTurboModulesSecurityCoreModule.class.getName(), PackageVersion.VERSION);
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(BasicToken.class, BasicTokenMixin.class);
        context.setMixInAnnotations(StringToken.class, StringTokenMixin.class);
    }

}
