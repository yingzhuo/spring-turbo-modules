/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import spring.turbo.module.queryselector.formatter.SelectorFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 应卓
 * @since 2.0.1
 */
@Data
@ConfigurationProperties(prefix = "springturbo.selector-set-formatter")
public class SelectorSetProperties {

    /**
     * 是否启用本插件
     */
    private boolean enabled = true;

    /**
     * 多个 {@link spring.turbo.module.queryselector.Selector} 之间的分隔符
     */
    private String separatorBetweenSelectors = "@@";

    /**
     * {@link SelectorFormatter} 配置
     */
    private SelectorProperties selectorFormatter = new SelectorProperties();

    /**
     * 解析失败是否忽略错误
     */
    private boolean ignoreErrorIfUnableToParse = false;

    /**
     * 打印失败是否忽略错误
     */
    private boolean ignoreErrorIfUnableToPrint = false;

    /**
     * SQL片段转换工具相关
     */
    private SQL sql = new SQL();

    @Data
    public static class SelectorProperties {

        /**
         * selector内部分隔符
         */
        private String separatorInSelector = "#";

        /**
         * range内部的分隔符
         */
        private String separatorInRange = "<==>";

        /**
         * set内部的分隔符
         */
        private String separatorInSet = "'";

        /**
         * date格式
         */
        private String datePattern = "yyyy-MM-dd";

        /**
         * datetime格式
         */
        private String datetimePattern = "yyyy-MM-dd HH:mm:ss";
    }

    @Data
    public static class SQL {

        /**
         * 是否启用SQL片段转换工具
         */
        private boolean enabled = true;

        /**
         * item 到 数据库column的映射
         */
        private Map<String, String> itemNameToTableColumnMappings = new HashMap<>();
    }

}