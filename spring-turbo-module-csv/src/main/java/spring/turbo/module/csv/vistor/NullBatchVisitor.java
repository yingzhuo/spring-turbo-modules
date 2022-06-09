/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.csv.vistor;

/**
 * @param <T> ValueObject泛型
 * @author 应卓
 * @since 1.0.9
 */
public final class NullBatchVisitor<T> implements BatchVisitor<T> {

    private NullBatchVisitor() {
        super();
    }

    public static <T> NullBatchVisitor<T> getInstance() {
        return SyncAvoid.INSTANCE;
    }

    // 延迟加载
    private static class SyncAvoid {
        public static final NullBatchVisitor INSTANCE = new NullBatchVisitor();
    }

}
