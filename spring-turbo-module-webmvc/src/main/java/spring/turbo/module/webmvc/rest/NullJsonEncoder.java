/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.rest;

/**
 * @author 应卓
 * @since 1.2.2
 */
public final class NullJsonEncoder implements JsonEncoder {

    public static NullJsonEncoder getInstance() {
        return SyncAvoid.INSTANCE;
    }

    /**
     * 私有构造方法
     */
    private NullJsonEncoder() {
        super();
    }

    @Override
    public String encode(String json) {
        return json;
    }

    // 延迟加载
    private static class SyncAvoid {
        private static final NullJsonEncoder INSTANCE = new NullJsonEncoder();
    }
}
