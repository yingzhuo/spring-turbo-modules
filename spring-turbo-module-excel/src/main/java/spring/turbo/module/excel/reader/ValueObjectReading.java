/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import spring.turbo.bean.valueobject.Alias;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;

import java.lang.annotation.*;

import static spring.turbo.module.excel.ExcelType.XSSF;
import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Component
public @interface ValueObjectReading {

    @AliasFor(annotation = Component.class, attribute = "value")
    public String value() default EMPTY;

    public String discriminatorValue();

    public Class<?> valueObjectType();

    public ExcelType excelType() default XSSF;

    public Header[] headers();

    public Alias[] aliases() default {};

    public int[] includeSheetIndex() default {};

    public ExcludeRowSet[] excludeRowSets() default {};

    public ExcludeRowRange[] excludeRowRanges() default {};

    public Class<? extends GlobalCellParser> globalCellParser() default DefaultCellParser.class;

    public ColumnCellParser[] columnCellParsers() default {};

    public Class<? extends Validator>[] additionalValidators() default {};

    public String password() default EMPTY;

    public boolean excludeAllNullRow() default true;

    public Class<? extends AdditionalConfiguration>[] additionalConfigurations() default {};

}
