package spring.turbo.module.security.x509;

import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import spring.turbo.util.text.StringMatcher;
import spring.turbo.util.text.TextVariables;

import java.security.cert.X509Certificate;
import java.util.Optional;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class SubjectDnX509PrincipalExtractor implements X509PrincipalExtractor {

    private final String name;

    public SubjectDnX509PrincipalExtractor() {
        this(SubjectDN.CN);
    }

    public SubjectDnX509PrincipalExtractor(@Nullable SubjectDN dn) {
        this.name = dn == null ? SubjectDN.CN.toString() : dn.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object extractPrincipal(X509Certificate clientCertificate) {
        var subjectDN = clientCertificate.getSubjectX500Principal().getName();
        var variables = new TextVariables(subjectDN, StringMatcher.charMatcher(','));
        variables.put("createdTime", String.valueOf(System.currentTimeMillis()));

        var principal = variables.getVariableValue(this.name, EMPTY);
        return Optional.ofNullable(principal).orElse(EMPTY);
    }

}
