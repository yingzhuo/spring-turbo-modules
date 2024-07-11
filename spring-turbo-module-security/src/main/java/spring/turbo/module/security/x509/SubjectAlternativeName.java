package spring.turbo.module.security.x509;

/**
 * @author 应卓
 * @see SubjectDnX509PrincipalExtractor
 * @since 3.3.1
 */
public enum SubjectAlternativeName {

    /**
     * common name 或 host name
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
     * 组织 (organization)
     */
    O,

    /**
     * 组织单位 (Organization Unit)
     */
    OU;

}
