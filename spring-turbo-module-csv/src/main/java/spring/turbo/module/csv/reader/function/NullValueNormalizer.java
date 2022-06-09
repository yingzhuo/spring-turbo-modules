/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.csv.reader.function;

/**
 * @author 应卓
 * @see #getInstance()
 * @since 1.0.13
 */
public final class NullValueNormalizer implements ValueNormalizer, GlobalValueNormalizer {

    /**
     * 私有构造方法
     */
    private NullValueNormalizer() {
        super();
    }

    public static NullValueNormalizer getInstance() {
        return AsyncAvoid.INSTANCE;
    }

    @Override
    public String normalize(String string) {
        return string;
    }

    // 延迟加载
    private static class AsyncAvoid {
        private static final NullValueNormalizer INSTANCE = new NullValueNormalizer();
    }

}
