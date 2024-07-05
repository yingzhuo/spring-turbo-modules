package spring.turbo.module.security.user;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import spring.turbo.module.security.authentication.RequestDetailsProvider;

import java.lang.annotation.*;

/**
 * 获取当前用户的Details对象
 *
 * @author 应卓
 * @see org.springframework.security.core.context.SecurityContext
 * @see AbstractAuthenticationToken#getDetails()
 * @see RequestDetailsProvider
 * @see RequestDetailsProvider#SPRING_SECURITY_DEFAULT
 * @since 1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@CurrentSecurityContext(expression = "authentication.details")
public @interface CurrentDetails {

    @AliasFor(annotation = CurrentSecurityContext.class, attribute = "errorOnInvalidType")
    public boolean errorOnInvalidType() default false;

}
