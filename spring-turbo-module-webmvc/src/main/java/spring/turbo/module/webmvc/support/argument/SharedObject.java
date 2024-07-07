package spring.turbo.module.webmvc.support.argument;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @see spring.turbo.util.ThreadSharedObjects
 * @see SharedObjectHandlerMethodArgumentResolver
 * @see spring.turbo.util.ThreadSharedObjects
 * @since 3.3.1
 */
@Inherited
@Documented
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SharedObject {

    public String name() default "";

    public Class<?> type() default void.class;

}
