package spring.turbo.module.jackson.module;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class CommonModule extends SimpleModule {

    /**
     * 默认构造方法
     */
    public CommonModule() {
        super(CommonModule.class.getName());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        // 这个Module实际上也没有任何配置
    }

}
