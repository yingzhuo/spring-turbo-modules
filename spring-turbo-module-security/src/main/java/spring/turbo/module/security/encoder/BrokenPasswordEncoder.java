/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 应卓
 * @see #getInstance()
 * @see EncodingIds#BROKEN
 * @since 2.0.14
 */
public final class BrokenPasswordEncoder implements PasswordEncoder {

    /**
     * 私有构造方法
     */
    private BrokenPasswordEncoder() {
        super();
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static BrokenPasswordEncoder getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        throw new UnsupportedOperationException("encode is not supported");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
        throw new IllegalArgumentException("There is no PasswordEncoder mapped for the id.");
    }

    // 延迟加载
    private static final class SyncAvoid {
        private static final BrokenPasswordEncoder INSTANCE = new BrokenPasswordEncoder();
    }

}
