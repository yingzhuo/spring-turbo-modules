package spring.turbo.module.security.encoder;

/**
 * @author 应卓
 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder(String)
 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder(String, String)
 * @since 1.1.0
 */
public final class EncodingIds {

    /**
     * Noop
     */
    public static final String noop = "noop";

    /**
     * bcrypt
     */
    public static final String bcrypt = "bcrypt";

    /**
     * ldap
     */
    public static final String ldap = "ldap";

    /**
     * pbkdf2
     */
    public static final String pbkdf2 = "pbkdf2";

    /**
     * scrypt
     */
    public static final String scrypt = "scrypt";

    /**
     * argon2
     */
    public static final String argon2 = "argon2";

    /**
     * sha1
     */
    public static final String SHA_1 = "SHA-1";

    /**
     * sha256
     */
    public static final String SHA_256 = "SHA-256";

    /**
     * sha384
     */
    public static final String SHA_384 = "SHA-384";

    /**
     * sha512
     */
    public static final String SHA_512 = "SHA-512";

    /**
     * MD2
     */
    public static final String MD2 = "MD2";

    /**
     * MD4
     */
    public static final String MD4 = "MD4";

    /**
     * MD5
     */
    public static final String MD5 = "MD5";

    /**
     * SM3 (国密)
     */
    public static final String SM3 = "SM3";

    /**
     * 私有构造方法
     */
    private EncodingIds() {
    }

}
