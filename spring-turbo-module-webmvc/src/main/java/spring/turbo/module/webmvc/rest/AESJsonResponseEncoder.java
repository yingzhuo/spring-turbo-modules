/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.rest;

import spring.turbo.module.crypto.AES;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 *
 * @since 1.2.2
 */
public class AESJsonResponseEncoder implements JsonResponseEncoder {

    private final AES aes;

    public AESJsonResponseEncoder(AES aes) {
        Asserts.notNull(aes);
        this.aes = aes;
    }

    public AESJsonResponseEncoder(AES.Mode mode, String password, String salt) {
        this.aes = AES.builder().mode(mode).passwordAndSalt(password, salt).build();
    }

    @Override
    public String encode(String jsonContent) {
        return aes.encrypt(jsonContent);
    }

}
