/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
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
public @interface RestClientInterface {

    /**
     * Bean Name
     *
     * @return Bean Name
     * @see #beanName()
     */
    @AliasFor("beanName")
    public String value() default "";

    /**
     * Bean Name
     *
     * @return Bean Name
     * @see #value()
     */
    @AliasFor("value")
    public String beanName() default "";

    /**
     * Bean qualifiers
     *
     * @return Bean qualifiers
     * @see Qualifier
     */
    public String[] qualifiers() default {};

    /**
     * 是否为primary
     *
     * @return primary
     * @see org.springframework.context.annotation.Primary
     */
    public boolean primary() default false;

    /**
     * 返回 {@link org.springframework.web.client.RestClient} 提供器类型
     *
     * @return {@link org.springframework.web.client.RestClient} 提供器类型
     * @see RestClientSupplier
     */
    public Class<? extends RestClientSupplier> clientSupplier() default RestClientSupplier.Default.class;

    public Class<? extends ArgumentResolversSupplier> argumentResolversSupplier() default ArgumentResolversSupplier.Default.class;

}
