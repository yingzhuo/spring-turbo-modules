package spring.turbo.module.jwt.alg;

/**
 * @author 应卓
 * @since 3.3.2
 */
@Deprecated
public enum JwtAlg {

    /**
     * HMAC using SHA-256
     */
    HS256,

    /**
     * HMAC using SHA-384
     */
    HS384,

    /**
     * HMAC using SHA-512
     */
    HS512,

    /**
     * ECDSA using P-256 and SHA-256
     */
    ES256,

    /**
     * ECDSA using P-384 and SHA-384
     */
    ES384,

    /**
     * ECDSA using P-512 and SHA-512
     */
    ES512,

    /**
     * RSASSA-PKCS-v1_5 using SHA-256
     */
    RS256,

    /**
     * RSASSA-PKCS-v1_5 using SHA-384
     */
    RS384,

    /**
     * RSASSA-PKCS-v1_5 using SHA-512
     */
    RS512,

    /**
     * RSASSA-PSS using SHA-256 and MGF1 with SHA-256
     */
    PS256,

    /**
     * RSASSA-PSS using SHA-384 and MGF1 with SHA-384
     */
    PS384,

    /**
     * RSASSA-PSS using SHA-512 and MGF1 with SHA-512
     */
    PS512,

    /**
     * Edwards-curve Digital Signature Algorithm
     */
    EdDSA

}
