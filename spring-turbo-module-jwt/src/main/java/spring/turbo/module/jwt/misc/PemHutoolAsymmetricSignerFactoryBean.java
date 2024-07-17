package spring.turbo.module.jwt.misc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ssl.pem.PemContent;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class PemHutoolAsymmetricSignerFactoryBean extends AbstractHutoolAsymmetricSignerFactoryBean
        implements InitializingBean, ResourceLoaderAware {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private String certificateLocation;

    private String certificateContent = "";

    private String keyLocation;

    private String keyContent = "";

    @Nullable
    private String keyPassword;


    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        var cert = getCert();
        var privateKey = getPrivateKey();

        if (cert == null || privateKey == null) {
            throw new IllegalStateException("cannot load private-key or certificate");
        }

        super.setKeyPair(cert.getPublicKey(), privateKey);
        super.setSigAlgName(cert.getSigAlgName());
    }

    @Nullable
    private PrivateKey getPrivateKey() {
        return Optional.ofNullable(getPrivatePemContent())
                .map(PemContent::getPrivateKey)
                .orElse(null);
    }

    @Nullable
    private X509Certificate getCert() {
        var pem = getCertificatePemContent();
        if (pem == null) {
            return null;
        }
        var xs = pem.getCertificates();
        if (xs.size() == 1) {
            return xs.get(0);
        }
        throw new IllegalStateException("certificates could be loaded");
    }

    @Nullable
    private PemContent getCertificatePemContent() {
        if (StringUtils.hasText(certificateContent)) {
            return PemContent.of(arrangePemContent(certificateContent));
        } else {
            try {
                return PemContent.of(resourceLoader.getResource(certificateLocation).getContentAsString(StandardCharsets.UTF_8));
            } catch (IOException e) {
                return null;
            }
        }
    }

    @Nullable
    private PemContent getPrivatePemContent() {
        if (StringUtils.hasText(keyContent)) {
            return PemContent.of(arrangePemContent(keyContent));
        } else {
            try {
                return PemContent.of(resourceLoader.getResource(keyLocation).getContentAsString(StandardCharsets.UTF_8));
            } catch (IOException e) {
                return null;
            }
        }
    }

    private String arrangePemContent(String text) {
        var iter = new StringTokenizer(text, "\n", false);
        var builder = new StringBuilder();
        while (iter.hasMoreTokens()) {
            var line = iter.nextToken();
            builder.append(line.trim());
            if (iter.hasMoreTokens()) {
                builder.append('\n');
            }
        }
        return builder.toString().trim();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setCertificateLocation(String certificateLocation) {
        this.certificateLocation = certificateLocation;
    }

    public void setKeyLocation(String keyLocation) {
        this.keyLocation = keyLocation;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public void setCertificateContent(String certificateContent) {
        this.certificateContent = certificateContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

}
