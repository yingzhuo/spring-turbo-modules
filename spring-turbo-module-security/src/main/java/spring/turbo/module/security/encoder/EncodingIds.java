/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.encoder;

/**
 * @author 应卓
 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder(String)
 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder(String, String)
 * @since 1.1.0
 */
public final class EncodingIds {

    public static final String noop = "noop";
    public static final String bcrypt = "bcrypt";
    public static final String ldap = "ldap";
    public static final String pbkdf2 = "pbkdf2";
    public static final String scrypt = "scrypt";
    public static final String argon2 = "argon2";
    public static final String morse = "morse";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_512 = "SHA-512";
    public static final String MD2 = "MD2";
    public static final String MD4 = "MD4";
    public static final String MD5 = "MD5";
    public static final String SM3 = "SM3";

    /**
     * 私有构造方法
     */
    private EncodingIds() {
        super();
    }

}
