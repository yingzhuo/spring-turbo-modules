package spring.turbo.module.security.encoder.hutool;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.turbo.module.security.encoder.PasswordEncoderFactories;

import static spring.turbo.module.security.encoder.EncodingIds.SM3;

/**
 * 国密算法 (SM3)
 *
 * @author 应卓
 * @see org.springframework.security.crypto.factory.PasswordEncoderFactories
 * @see PasswordEncoderFactories
 * @see spring.turbo.module.security.encoder.EncodingIds#SM3
 * @since 1.0.1
 */
public final class SM3PasswordEncoder implements PasswordEncoder {

    /**
     * 构造方法
     */
    public SM3PasswordEncoder() {
    }

    @Override
    public String encode(CharSequence rawPassword) {
        final Digester digester = DigestUtil.digester(SM3);
        return digester.digestHex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

}
