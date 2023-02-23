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
import spring.turbo.lang.Beta;
import spring.turbo.lang.Singleton;
import spring.turbo.util.HexUtils;

import static spring.turbo.util.CharsetPool.UTF_8;

/**
 * HEX算法的密码Encoder
 *
 * @author 应卓
 * @see PasswordEncoder
 * @since 2.0.0
 */
@Beta
@Singleton
public final class HEXPasswordEncoder implements PasswordEncoder {

    /**
     * 私有构造方法
     */
    private HEXPasswordEncoder() {
        super();
    }

    /**
     * 获取单例实例
     *
     * @return 实例
     */
    public static HEXPasswordEncoder getInstance() {
        return SyncAvoid.INSTANCE;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return new String(HexUtils.encode(rawPassword.toString().getBytes(UTF_8)));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

    // 延迟加载
    private static class SyncAvoid {
        private static final HEXPasswordEncoder INSTANCE = new HEXPasswordEncoder();
    }

}
