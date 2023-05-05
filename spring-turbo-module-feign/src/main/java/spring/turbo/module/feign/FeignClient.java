/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;
import spring.turbo.module.feign.annotation.*;

import java.lang.annotation.*;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 *
 * @see Logging
 * @see EncoderAndDecoder
 * @see ErrorDecoder
 * @see QueryMapEncoder
 * @see Client
 * @see Contract
 * @see Capabilities
 * @see Options
 * @see DoNotCloseAfterDecode
 * @see RequestInterceptors
 * @see Customizer
 * @see Decoded404
 *
 * @since 1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Qualifier
public @interface FeignClient {

    /**
     * Bean Name
     *
     * @return Bean Name
     *
     * @see #beanName()
     */
    @AliasFor("beanName")
    public String value() default EMPTY;

    /**
     * Bean Name
     *
     * @return Bean Name
     *
     * @see #value()
     */
    @AliasFor("value")
    public String beanName() default EMPTY;

    /**
     * URL
     *
     * @return url
     */
    public String url() default EMPTY;

    /**
     * Bean qualifiers
     *
     * @return Bean qualifiers
     *
     * @see Qualifier
     */
    public String[] qualifiers() default {};

    /**
     * 是否为primary
     *
     * @return primary
     *
     * @see org.springframework.context.annotation.Primary
     */
    public boolean primary() default true;

}
