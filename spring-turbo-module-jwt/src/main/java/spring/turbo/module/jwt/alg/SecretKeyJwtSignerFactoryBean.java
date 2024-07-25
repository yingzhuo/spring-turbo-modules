package spring.turbo.module.jwt.alg;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import static org.springframework.util.StringUtils.hasText;
import static spring.turbo.module.jwt.alg.JwtSignerFactories.createFromBase64EncodedString;
import static spring.turbo.module.jwt.alg.JwtSignerFactories.createFromBase64URlEncodedString;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class SecretKeyJwtSignerFactoryBean implements FactoryBean<SecretKeyJwtSigner> {

    @Nullable
    private String base64EncodedString;

    @Nullable
    private String base64URLEncodedString;

    /**
     * {@inheritDoc}
     */
    @Override
    public SecretKeyJwtSigner getObject() {
        if (hasText(base64EncodedString)) {
            return createFromBase64EncodedString(base64EncodedString);
        }

        if (hasText(base64URLEncodedString)) {
            return createFromBase64URlEncodedString(base64URLEncodedString);
        }

        throw new IllegalStateException("invalid configuration");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return SecretKeyJwtSigner.class;
    }

    public void setBase64EncodedString(@Nullable String base64EncodedString) {
        this.base64EncodedString = base64EncodedString;
    }

    public void setBase64URLEncodedString(@Nullable String base64URLEncodedString) {
        this.base64URLEncodedString = base64URLEncodedString;
    }

}
