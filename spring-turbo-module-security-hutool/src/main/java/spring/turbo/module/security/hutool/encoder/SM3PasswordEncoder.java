/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.encoder;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import spring.turbo.module.security.encoder.AbstractNamedPasswordEncoder;
import spring.turbo.module.security.encoder.PasswordEncoderFactories;

import static spring.turbo.module.security.encoder.EncodingIds.SM3;

/**
 * 国密算法 (SM3)
 *
 * @author 应卓
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @since 1.0.1
 */
public final class SM3PasswordEncoder extends AbstractNamedPasswordEncoder {

    public SM3PasswordEncoder() {
        super(SM3);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        final Digester digester = DigestUtil.digester(SM3);
        return digester.digestHex(rawPassword.toString());
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
