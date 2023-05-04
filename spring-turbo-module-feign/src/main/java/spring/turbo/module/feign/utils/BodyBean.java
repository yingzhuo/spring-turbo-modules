/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign.utils;

import spring.turbo.util.StringPool;

import java.util.LinkedHashMap;

/**
 * @author 应卓
 *
 * @see feign.Body
 *
 * @since 1.0.0
 */
public final class BodyBean extends LinkedHashMap<String, Object> {

    private BodyBean() {
        super();
    }

    public static BodyBean newInstance() {
        return new BodyBean();
    }

    public BodyBean add(String name, Object value) {
        this.put(name, value);
        return this;
    }

    public BodyBean addEmpty(String name) {
        return add(name, StringPool.EMPTY);
    }

}
