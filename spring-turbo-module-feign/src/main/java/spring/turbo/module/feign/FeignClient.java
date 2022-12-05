/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import spring.turbo.module.feign.annotation.*;
import spring.turbo.util.StringPool;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @see Logging
 * @see EncoderAndDecoder
 * @see ErrorDecoder
 * @see QueryMapEncoder
 * @see Decoded404
 * @see Client
 * @see Contract
 * @see Capabilities
 * @see Options
 * @see DoNotCloseAfterDecode
 * @see RequestInterceptors
 * @see Customizer
 * @since 1.0.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FeignClient {

    /**
     * Bean Name
     *
     * @return Bean Name
     */
    public String value() default StringPool.EMPTY;

    /**
     * URL
     *
     * @return url
     */
    public String url();

}
