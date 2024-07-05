package spring.turbo.module.webmvc.support.request;

import java.lang.annotation.*;

/**
 * {@code @Versioned(value = "v2", ignoreCase = true)} 的快捷方式
 *
 * @author 应卓
 * @since 2.1.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Versioned("v2")
public @interface API_v2 {
}
