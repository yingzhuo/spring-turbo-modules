/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.util.Asserts;

/**
 * {@link NamedPasswordEncoder}默认实现
 *
 * @author 应卓
 * @see NamedPasswordEncoder
 * @see NamedPasswordEncoderProvider
 * @see spring.turbo.module.security.integration.NamedPasswordEncoderProviderImpl
 * @since 1.0.2
 */
class DelegatingNamedPasswordEncoder implements NamedPasswordEncoder {

    private final String name;
    private final PasswordEncoder passwordEncoder;

    public DelegatingNamedPasswordEncoder(@NonNull String name, @NonNull PasswordEncoder passwordEncoder) {
        Asserts.hasText(name);
        Asserts.notNull(passwordEncoder);
        this.name = name;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return this.passwordEncoder.upgradeEncoding(encodedPassword);
    }

}
