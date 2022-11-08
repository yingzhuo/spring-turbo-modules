/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.user;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import spring.turbo.module.security.authentication.RequestDetailsBuilder;

import java.lang.annotation.*;

/**
 * 获取当前用户的Details对象
 *
 * @author 应卓
 * @see org.springframework.security.core.context.SecurityContext
 * @see AbstractAuthenticationToken#getDetails()
 * @see RequestDetailsBuilder
 * @see RequestDetailsBuilder#SPRING_SECURITY_DEFAULT
 * @see RequestDetailsBuilder#SNAPSHOT
 * @see RequestDetailsBuilder#DESCRIPTION
 * @since 1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@CurrentSecurityContext(expression = "authentication.details")
public @interface CurrentDetails {

    @AliasFor(annotation = CurrentSecurityContext.class, attribute = "errorOnInvalidType")
    public boolean errorOnInvalidType() default false;

}
