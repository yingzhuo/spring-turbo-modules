/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import spring.turbo.bean.Pair;
import spring.turbo.lang.Mutable;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Mutable
public final class Headers implements Serializable {

    private final Map<String, Pair<Integer, String[]>> configTable = new HashMap<>();

    private Headers() {
        super();
    }

    public static Headers newInstance() {
        return new Headers();
    }

    public Headers add(String sheetName, int rowIndex) {
        Asserts.hasText(sheetName);
        Asserts.isTrue(rowIndex >= 0);

        configTable.put(sheetName, Pair.of(rowIndex, null));
        return this;
    }

    public Map<String, Pair<Integer, String[]>> getConfigTable() {
        return configTable;
    }

    public boolean isEmpty() {
        return configTable.isEmpty();
    }

}
