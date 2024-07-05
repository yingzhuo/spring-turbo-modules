package spring.turbo.module.webmvc.support.argument;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @see spring.turbo.util.TreadSharedObjects
 * @see SharedObjectHandlerMethodArgumentResolver
 * @since 3.3.1
 */
@Inherited
@Documented
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SharedObject {

    public String byName() default "";

    public Class<?> byType() default void.class;

}
