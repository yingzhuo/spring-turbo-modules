/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.integration;

import spring.turbo.module.security.NamedPasswordEncoder;
import spring.turbo.module.security.NamedPasswordEncoderProvider;
import spring.turbo.module.security.hutool.encoder.MD2PasswordEncoder;
import spring.turbo.module.security.hutool.encoder.SM3PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class NamedPasswordEncoderProviderImpl implements NamedPasswordEncoderProvider {

    @Override
    public Collection<NamedPasswordEncoder> getPasswordEncoders() {
        final List<NamedPasswordEncoder> list = new ArrayList<>();
        list.add(new MD2PasswordEncoder());
        list.add(new SM3PasswordEncoder());
        return Collections.unmodifiableCollection(list);
    }

}
