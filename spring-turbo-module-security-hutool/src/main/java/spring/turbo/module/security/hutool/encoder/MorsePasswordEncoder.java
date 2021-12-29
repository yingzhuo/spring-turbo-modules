/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.encoder;

import cn.hutool.core.codec.Morse;
import org.springframework.lang.NonNull;
import spring.turbo.module.security.encoder.AbstractNamedPasswordEncoder;
import spring.turbo.util.Asserts;

import static spring.turbo.util.CharPool.*;

/**
 * 莫斯密电码
 *
 * @author 应卓
 * @since 1.0.2
 */
public final class MorsePasswordEncoder extends AbstractNamedPasswordEncoder {

    private final Morse morse;

    public MorsePasswordEncoder() {
        this(new Morse(DOT, HYPHEN, SLASH));
    }

    public MorsePasswordEncoder(@NonNull Morse morse) {
        super("morse");
        Asserts.notNull(morse);
        this.morse = morse;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return morse.encode(rawPassword.toString());
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return true;
    }

}
