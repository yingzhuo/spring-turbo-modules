package spring.turbo.module.jwt.alg;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import static org.springframework.util.StringUtils.hasText;
import static spring.turbo.module.jwt.alg.JwtSignerFactories.*;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class JwtSignerFactoryBean implements FactoryBean<JwtSigner> {

    @Nullable
    private String base64EncodedString;

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

    @Override
    public JwtSigner getObject() {
        if (hasText(base64EncodedString)) {
            return createFromBase64EncodedString(base64EncodedString);
        }

        if (hasText(certificateLocation) && hasText(privateKeyLocation)) {
            return createFromPemResource(certificateLocation, privateKeyLocation, privateKeyPassword);
        }

        if (hasText(certificateContent) && hasText(privateKeyContent)) {
            return createFromPemContent(certificateContent, privateKeyContent, privateKeyPassword);
        }

        throw new IllegalStateException("invalid configuration");
    }

    @Override
    public Class<?> getObjectType() {
        return JwtSigner.class;
    }

    public void setBase64EncodedString(@Nullable String base64EncodedString) {
        this.base64EncodedString = base64EncodedString;
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
