package spring.turbo.module.security.encoder.hutool;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.module.security.encoder.PasswordEncoderFactories;

/**
 * SHA-512 算法实现 {@link PasswordEncoder}
 *
 * @author 应卓
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @see spring.turbo.module.security.encoder.EncodingIds#SHA_512
 * @since 1.0.1
 */
public final class SHA512PasswordEncoder implements PasswordEncoder {

    /**
     * 构造方法
     */
    public SHA512PasswordEncoder() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(CharSequence rawPassword) {
        final Digester digester = new Digester(DigestAlgorithm.SHA512);
        return digester.digestHex(rawPassword.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

}
