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
public final class JsonEncoderFactories {

    /**
     * 私有构造方法
     */
    private JsonEncoderFactories() {
        super();
    }

    public static JsonEncoder noop() {
        return NullJsonEncoder.getInstance();
    }

    public static JsonEncoder aes(AES.Mode mode, String password, String salt) {
        return new AESJsonEncoder(mode, password, salt);
    }

}
