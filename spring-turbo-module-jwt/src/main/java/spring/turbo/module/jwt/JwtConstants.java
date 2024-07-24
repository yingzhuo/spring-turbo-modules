package spring.turbo.module.jwt;

/**
 * Jwt相关常量
 *
 * @author 应卓
 * @since 3.3.2
 */
public final class JwtConstants {

    // headers
    // -----------------------------------------------------------------------------------------------------------------
    public static final String HEADER_TYPE = "typ";
    public static final String HEADER_KEY_ID = "kid";
    public static final String HEADER_ALGORITHM = "alg";
    public static final String HEADER_CONTENT_TYPE = "cty";

    // payload
    // -----------------------------------------------------------------------------------------------------------------
    public static final String PAYLOAD_ISSUER = "iss";
    public static final String PAYLOAD_SUBJECT = "sub";
    public static final String PAYLOAD_AUDIENCE = "aud";
    public static final String PAYLOAD_EXPIRES = "exp";
    public static final String PAYLOAD_NOT_BEFORE = "nbf";
    public static final String PAYLOAD_ISSUED_AT = "iat";
    public static final String PAYLOAD_JWT_ID = "jti";

    /**
     * HMAC using SHA-256
     */
    public static final String ALG_HS256 = "HS256";

    /**
     * HMAC using SHA-384
     */
    public static final String ALG_HS384 = "HS384";

    /**
     * HMAC using SHA-512
     */
    public static final String ALG_HS512 = "HS512";

    /**
     * ECDSA using P-256 and SHA-256
     */
    public static final String ALG_ES256 = "ES256";

    /**
     * ECDSA using P-384 and SHA-384
     */
    public static final String ALG_ES384 = "ES384";

    /**
     * ECDSA using P-512 and SHA-512
     */
    public static final String ALG_ES512 = "ES512";

    /**
     * RSASSA-PKCS-v1_5 using SHA-256
     */
    public static final String ALG_RS256 = "RS256";

    /**
     * RSASSA-PKCS-v1_5 using SHA-384
     */
    public static final String ALG_RS384 = "RS384";

    /**
     * RSASSA-PKCS-v1_5 using SHA-512
     */
    public static final String ALG_RS512 = "RS512";

    /**
     * RSASSA-PSS using SHA-256 and MGF1 with SHA-256
     */
    public static final String ALG_PS256 = "PS256";

    /**
     * RSASSA-PSS using SHA-384 and MGF1 with SHA-384
     */
    public static final String ALG_PS384 = "PS384";

    /**
     * RSASSA-PSS using SHA-512 and MGF1 with SHA-512
     */
    public static final String ALG_PS512 = "PS512";

    /**
     * Edwards-curve Digital Signature Algorithm
     */
    public static final String ALG_EdDSA = "EdDSA";

    /**
     * 私有构造方法
     */
    private JwtConstants() {
    }

}
