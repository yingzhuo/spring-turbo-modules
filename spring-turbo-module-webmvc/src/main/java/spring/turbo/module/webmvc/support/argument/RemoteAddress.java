package spring.turbo.module.webmvc.support.argument;

import java.lang.annotation.*;

/**
 * 客户端IP地址
 *
 * @author 应卓
 * @see RemoteAddressHandlerMethodArgumentResolver
 * @since 1.0.0
 */
@Inherited
@Documented
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteAddress {
}
