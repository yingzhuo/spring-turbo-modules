/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.encoder;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import spring.turbo.module.security.encoder.AbstractNamedPasswordEncoder;
import spring.turbo.module.security.encoder.PasswordEncoderFactories;

import static spring.turbo.module.security.encoder.EncodingIds.SHA_384;

/**
 * @author 应卓
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @since 1.0.1
 */
public final class SHA384PasswordEncoder extends AbstractNamedPasswordEncoder {

    public SHA384PasswordEncoder() {
        super(SHA_384);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        final Digester digester = new Digester(DigestAlgorithm.SHA384);
        return digester.digestHex(rawPassword.toString());
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }

}
