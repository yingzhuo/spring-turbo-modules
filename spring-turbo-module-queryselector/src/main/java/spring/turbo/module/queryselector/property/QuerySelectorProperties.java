/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "springturbo.queryselector")
public class QuerySelectorProperties implements Serializable {

    /**
     * 是否启用本插件
     */
    private boolean enabled = true;

    /**
     * selector之间的分隔符
     */
    private String separatorBetweenSelectors = "@@";

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

    /**
     * 解析失败是否忽略错误
     */
    private boolean skipErrorIfUnableToResolve = false;

}
