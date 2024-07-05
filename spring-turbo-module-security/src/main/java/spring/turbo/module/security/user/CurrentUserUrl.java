package spring.turbo.module.security.user;

import org.springframework.core.annotation.AliasFor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;

import java.lang.annotation.*;

/**
 * 获取当前用户的URL
 *
 * @author 应卓
 * @see UserDetails
 * @see UserDetailsPlus
 * @see UserDetailsPlus#getUrl()
 * @see AuthenticationPrincipalArgumentResolver
 * @since 1.1.2
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#root.url")
public @interface CurrentUserUrl {

    @AliasFor(annotation = AuthenticationPrincipal.class, attribute = "errorOnInvalidType")
    public boolean errorOnInvalidType() default false;

}
