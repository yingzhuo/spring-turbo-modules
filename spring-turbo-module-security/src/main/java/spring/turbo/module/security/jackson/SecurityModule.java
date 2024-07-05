package spring.turbo.module.security.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import spring.turbo.module.security.token.BasicToken;
import spring.turbo.module.security.token.StringToken;

/**
 * @author 应卓
 * @since 2.0.3
 */
public class SecurityModule extends SimpleModule {

    /**
     * 默认构造方法
     */
    public SecurityModule() {
        super(SecurityModule.class.getName());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(StringToken.class, StringTokenMixin.class);
        context.setMixInAnnotations(BasicToken.class, BasicTokenMixin.class);
    }

}
