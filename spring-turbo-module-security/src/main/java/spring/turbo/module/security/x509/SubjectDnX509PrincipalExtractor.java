package spring.turbo.module.security.x509;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import spring.turbo.util.StringPool;
import spring.turbo.util.text.StringMatcher;
import spring.turbo.util.text.TextVariables;

import java.security.cert.X509Certificate;
import java.util.Objects;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class SubjectDnX509PrincipalExtractor implements X509PrincipalExtractor {

    private static final Logger logger = LoggerFactory.getLogger(SubjectDnX509PrincipalExtractor.class);

    private final String subjectAlternativeName;

    public SubjectDnX509PrincipalExtractor() {
        this(SubjectAlternativeName.CN);
    }

    public SubjectDnX509PrincipalExtractor(@Nullable SubjectAlternativeName subjectAlternativeName) {
        this.subjectAlternativeName =
                Objects.requireNonNullElse(subjectAlternativeName, SubjectAlternativeName.CN).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object extractPrincipal(X509Certificate clientCert) {
        var subjectDN = clientCert.getSubjectX500Principal().getName();
        var variables = new TextVariables(subjectDN, StringMatcher.charMatcher(','));
        logger.debug("Subject DN: '{}'", variables);
        var principal = variables.getVariableValue(this.subjectAlternativeName, StringPool.EMPTY);
        logger.debug("principal: '{}'", principal);
        return principal;
    }

}
