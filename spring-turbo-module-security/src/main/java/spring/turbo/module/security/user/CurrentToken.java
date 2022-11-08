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
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;

import java.lang.annotation.*;

/**
 * 获取当前用户的携带令牌 {@link spring.turbo.webmvc.token.Token}类型
 *
 * @author 应卓
 * @see spring.turbo.webmvc.token.Token
 * @see org.springframework.security.core.Authentication
 * @see SecurityContext#getAuthentication()
 * @since 1.2.3
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@CurrentSecurityContext(expression = "authentication.token")
public @interface CurrentToken {

    @AliasFor(annotation = CurrentSecurityContext.class, attribute = "errorOnInvalidType")
    public boolean errorOnInvalidType() default false;

}
