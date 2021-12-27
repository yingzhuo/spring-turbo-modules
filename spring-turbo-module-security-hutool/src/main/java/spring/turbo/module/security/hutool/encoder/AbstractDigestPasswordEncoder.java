/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.encoder;

import spring.turbo.module.security.encoder.NamedPasswordEncoder;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.1
 */
public abstract class AbstractDigestPasswordEncoder implements NamedPasswordEncoder {

    private final String name;

    public AbstractDigestPasswordEncoder(String name) {
        Asserts.hasText(name);
        this.name = name;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public abstract String encode(CharSequence rawPassword);

    @Override
    public final boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

}
