/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.sql;

import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.SelectorSet;

import java.util.Collections;
import java.util.Map;

/**
 * @author 应卓
 *
 * @since 2.0.1
 */
@FunctionalInterface
public interface WhereClauseBuilder {

    public default String apply(@Nullable SelectorSet selectors) {
        return apply(selectors, Collections.emptyMap());
    }

    public String apply(@Nullable SelectorSet selectors, @Nullable Map<String, String> itemNameToTableColumnMap);

}
