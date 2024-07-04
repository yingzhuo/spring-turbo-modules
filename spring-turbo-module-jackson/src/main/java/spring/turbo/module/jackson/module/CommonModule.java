/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
