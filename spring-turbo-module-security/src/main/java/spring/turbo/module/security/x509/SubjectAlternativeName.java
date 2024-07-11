package spring.turbo.module.security.x509;

import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;

/**
 * @author 应卓
 * @see X509PrincipalExtractor
 * @see SubjectDnX509PrincipalExtractor
 * @since 3.3.1
 */
public enum SubjectAlternativeName {

    /**
     * 通用名 (Common Name)
     */
    CN,

    /**
     * 国家 (Country)
     */
    C,

    /**
     * 州 / 省 (State)
     */
    ST,

    /**
     * 地区 / 城市 (Location)
     */
    L,

    /**
     * 组织 (Organization)
     */
    O,

    /**
     * 组织单位 (Organization Unit)
     */
    OU

}
