/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.util;

import spring.turbo.core.SpringUtils;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.sql.WhereClauseBuilder;

import java.util.Map;

/**
 * @author 应卓
 * @since 1.1.2
 */
public final class WhereClauseUtils {

    /**
     * 私有构造方法
     */
    private WhereClauseUtils() {
        super();
    }

    public static String generateSqlSnippet(SelectorSet selectors) {
        return SpringUtils.getRequiredBean(WhereClauseBuilder.class).apply(selectors);
    }

    public static String generateSqlSnippet(SelectorSet selectors, Map<String, String> itemNameToTableColumnMap) {
        return SpringUtils.getRequiredBean(WhereClauseBuilder.class).apply(selectors, itemNameToTableColumnMap);
    }

}
