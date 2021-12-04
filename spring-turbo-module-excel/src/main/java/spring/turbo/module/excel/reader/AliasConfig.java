/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import spring.turbo.lang.Mutable;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Mutable
public final class AliasConfig extends HashMap<String, String> implements Serializable {

    private AliasConfig() {
        super();
    }

    public static AliasConfig newInstance() {
        return new AliasConfig();
    }

    public AliasConfig add(String from, String to) {
        Asserts.hasText(from);
        Asserts.hasText(to);
        this.put(from, to);
        return this;
    }

}
