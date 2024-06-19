/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.support;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

import static spring.turbo.util.StringPool.EMPTY;

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
    @AliasFor("beanName") String value() default EMPTY;

    /**
     * Bean Name
     *
     * @return Bean Name
     * @see #value()
     */
    @AliasFor("value") String beanName() default EMPTY;

    /**
     * Bean qualifiers
     *
     * @return Bean qualifiers
     * @see Qualifier
     */
    String[] qualifiers() default {};

    /**
     * 是否为primary
     *
     * @return primary
     * @see org.springframework.context.annotation.Primary
     */
    boolean primary() default true;

    /**
     * @see RestClientSupplier
     */
    Class<? extends RestClientSupplier> clientSupplier() default RestClientSupplier.Default.class;

}
