package spring.turbo.module.security.user;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;

import java.lang.annotation.*;

/**
 * 获取当前用户的携带令牌的值 {@link java.lang.String}类型
 *
 * @author 应卓
 * @see spring.turbo.module.security.token.Token
 * @see org.springframework.security.core.Authentication
 * @see SecurityContext#getAuthentication()
 * @since 1.2.3
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@CurrentSecurityContext(expression = "authentication.token.asString()")
public @interface CurrentRawToken {

    @AliasFor(annotation = CurrentSecurityContext.class, attribute = "errorOnInvalidType")
    public boolean errorOnInvalidType() default false;

}
