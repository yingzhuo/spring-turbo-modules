/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import spring.turbo.module.excel.CellParser;
import spring.turbo.module.excel.DefaultCellParser;
import spring.turbo.module.excel.ExcelType;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface ValueObjectReading {

    @AliasFor(annotation = Component.class, attribute = "value")
    public String value() default "";

    public String discriminatorValue();

    public Class<?> valueObjectType();

    public ExcelType excelType() default ExcelType.XSSF;

    public Header[] headers();

    public int[] includeSheetIndex() default {};

    public ExcludeRowSet[] excludeRowSets() default {};

    public ExcludeRowRange[] excludeRowRanges() default {};

    public Class<? extends CellParser> cellParser() default DefaultCellParser.class;

}
