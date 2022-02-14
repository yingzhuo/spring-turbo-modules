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
 * @since 1.0.13
 */
public final class NullHeaderNormalizer implements HeaderNormalizer {

    /**
     * 私有构造方法
     */
    private NullHeaderNormalizer() {
        super();
    }

    public static NullHeaderNormalizer getInstance() {
        return AsyncAvoid.INSTANCE;
    }

    @Override
    public String normalize(String string) {
        return string;
    }

    private static class AsyncAvoid {
        private static final NullHeaderNormalizer INSTANCE = new NullHeaderNormalizer();
    }

}
