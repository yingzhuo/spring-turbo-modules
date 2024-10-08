package spring.turbo.module.webmvc.support.request;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @since 2.0.9
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Versioned {

    @AliasFor("version")
    public String value() default "";

    @AliasFor("value")
    public String version() default "";

    public boolean ignoreCase() default true;

}
