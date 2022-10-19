/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.integration;

import org.springframework.lang.NonNull;
import spring.turbo.module.security.encoder.NamedPasswordEncoder;
import spring.turbo.module.security.encoder.NamedPasswordEncoderProvider;
import spring.turbo.module.security.hutool.encoder.MD2PasswordEncoder;
import spring.turbo.module.security.hutool.encoder.SHA384PasswordEncoder;
import spring.turbo.module.security.hutool.encoder.SHA512PasswordEncoder;
import spring.turbo.module.security.hutool.encoder.SM3PasswordEncoder;
import spring.turbo.util.collection.ListFactories;

import java.util.Collection;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class NamedPasswordEncoderProviderImpl implements NamedPasswordEncoderProvider {

    @NonNull
    @Override
    public Collection<NamedPasswordEncoder> getPasswordEncoders() {
        return ListFactories.newUnmodifiableList(
                new SM3PasswordEncoder(),
                new MD2PasswordEncoder(),
                new SHA384PasswordEncoder(),
                new SHA512PasswordEncoder()
        );
    }

}
