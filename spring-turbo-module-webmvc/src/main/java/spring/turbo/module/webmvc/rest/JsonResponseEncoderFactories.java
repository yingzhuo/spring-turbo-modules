/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.rest;

import spring.turbo.util.crypto.AES;

/**
 * @author 应卓
 * @since 1.2.2
 */
public final class JsonResponseEncoderFactories {

    /**
     * 私有构造方法
     */
    private JsonResponseEncoderFactories() {
        super();
    }

    public static JsonResponseEncoder noop() {
        return s -> s;
    }

    public static JsonResponseEncoder aes(AES aes) {
        return new AESJsonResponseEncoder(aes);
    }

    public static JsonResponseEncoder aes(AES.Mode mode, String password, String salt) {
        return new AESJsonResponseEncoder(mode, password, salt);
    }

}
