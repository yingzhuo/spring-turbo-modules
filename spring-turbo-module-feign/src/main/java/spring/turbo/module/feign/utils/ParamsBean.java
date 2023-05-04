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
 * @see feign.QueryMap
 *
 * @since 1.0.0
 */
public final class ParamsBean extends LinkedHashMap<String, Object> {

    private ParamsBean() {
        super();
    }

    public static ParamsBean newInstance() {
        return new ParamsBean();
    }

    public ParamsBean add(String name, Object value) {
        this.put(name, value);
        return this;
    }

    public ParamsBean addEmpty(String name) {
        return add(name, StringPool.EMPTY);
    }

}
