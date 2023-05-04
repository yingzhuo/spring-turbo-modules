/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.jackson2;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import spring.turbo.jackson2.support.ParserJsonDeserializer;
import spring.turbo.jackson2.support.PrinterJsonSerializer;
import spring.turbo.module.queryselector.formatter.SelectorSetFormatter;

/**
 * @author 应卓
 *
 * @see SelectorSetFormatter
 *
 * @since 2.0.1
 */
@JsonSerialize(using = SelectorSetMixin.S.class)
@JsonDeserialize(using = SelectorSetMixin.D.class)
public abstract class SelectorSetMixin {

    /**
     * 序列化器
     */
    public static class S extends PrinterJsonSerializer {
        public S() {
            super(SelectorSetFormatter.class, new SelectorSetFormatter());
        }
    }

    /**
     * 反序列化器
     */
    public static class D extends ParserJsonDeserializer {
        public D() {
            super(SelectorSetFormatter.class, new SelectorSetFormatter());
        }
    }

}
