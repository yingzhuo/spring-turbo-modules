/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector;

import java.io.Serializable;

/**
 * selector数据类型
 *
 * @author 应卓
 *
 * @see Selector
 *
 * @since 2.0.1
 */
public enum DataType implements Serializable {

    /**
     * 数值类型
     */
    NUMBER,

    /**
     * 字符串类型
     */
    STRING,

    /**
     * 日期类型
     */
    DATE,

    /**
     * 日期事件类型
     */
    DATETIME

}
