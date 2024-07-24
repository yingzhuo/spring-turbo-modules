package spring.turbo.module.jwt.alg;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import static org.springframework.util.StringUtils.hasText;
import static spring.turbo.module.jwt.alg.JwtSignerFactories.createFromPemContent;
import static spring.turbo.module.jwt.alg.JwtSignerFactories.createFromPemResource;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class KeyPairPemJwtSignerFactoryBean implements FactoryBean<KeyPairJwtSigner> {

    @Nullable
    private String certificateLocation;

    @Nullable
    private String privateKeyLocation;

    @Nullable
    private String certificateContent;

    @Nullable
    private String privateKeyContent;

    @Nullable
    private String privateKeyPassword;

    /**
     * {@inheritDoc}
     */
    @Override
    public KeyPairJwtSigner getObject() {
        if (hasText(certificateLocation) && hasText(privateKeyLocation)) {
            return createFromPemResource(certificateLocation, privateKeyLocation, privateKeyPassword);
        }

        if (hasText(certificateContent) && hasText(privateKeyContent)) {
            return createFromPemContent(certificateContent, privateKeyContent, privateKeyPassword);
        }

        throw new IllegalStateException("invalid configuration");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return KeyPairJwtSigner.class;
    }

    public void setCertificateLocation(@Nullable String certificateLocation) {
        this.certificateLocation = certificateLocation;
    }

    public void setPrivateKeyLocation(@Nullable String privateKeyLocation) {
        this.privateKeyLocation = privateKeyLocation;
    }

    public void setCertificateContent(@Nullable String certificateContent) {
        this.certificateContent = certificateContent;
    }

    public void setPrivateKeyContent(@Nullable String privateKeyContent) {
        this.privateKeyContent = privateKeyContent;
    }

    public void setPrivateKeyPassword(@Nullable String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }
}
