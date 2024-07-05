package spring.turbo.module.webcli.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EnableRestClientInterfacesConfiguration.class)
public @interface EnableRestClientInterfaces {

    @AliasFor("basePackages")
    public String[] value() default {};

    @AliasFor("value")
    public String[] basePackages() default {};

    public Class<?>[] basePackageClasses() default {};

    public Class<? extends ArgumentResolversSupplier> globalArgumentResolversSupplier() default ArgumentResolversSupplier.Default.class;

}
