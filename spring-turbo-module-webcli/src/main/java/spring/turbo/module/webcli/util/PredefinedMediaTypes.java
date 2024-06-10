/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.util;

import org.springframework.http.MediaType;

/**
 * @author 应卓
 *
 * @since 3.3.1
 */
public final class PredefinedMediaTypes {

    public static final MediaType XML = MediaType.valueOf("text/xml");
    public static final MediaType XML_UTF8 = MediaType.valueOf("text/xml;charset=utf-8");
    public static final MediaType JSON = MediaType.APPLICATION_JSON;
    public static final MediaType JSON_UTF8 = MediaType.valueOf("application/json;charset=UTF-8");

    /**
     * 私有构造方法
     */
    private PredefinedMediaTypes() {
        super();
    }

}
