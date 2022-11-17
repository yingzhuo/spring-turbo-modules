/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.excel.reader.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @see BatchSize
 * @see BatchSize_JPA_Hibernate
 * @see ColumnBasedCellParser
 * @see GlobalCellParser
 * @see ExcludeRowSet
 * @see ExcludeRowRange
 * @see IncludeSheetSet
 * @see IncludeSheetPattern
 * @see Header
 * @see Headerless
 * @see Password
 * @see Type
 * @see spring.turbo.bean.valueobject.Alias
 * @see spring.turbo.bean.valueobject.Alias.List
 * @see AdditionalValidators
 * @see Filter
 * @see Customizer
 * @since 1.0.0
 */
@Component
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface BatchProcessor {

    @AliasFor(annotation = Component.class, attribute = "value")
    public String value() default EMPTY;

    public String discriminatorValue();

    public Class<?> valueObjectType();

}
