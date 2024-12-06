package spring.turbo.module.redis.aspect;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 防重复调用
 *
 * @author 应卓
 * @see AvoidRepeatedInvocationAdvice
 * @since 3.4.0
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AvoidRepeatedInvocation {

    /**
     * SpringEL 表达方法调用的唯一性
     *
     * @return SpEL
     */
    public String value();

    /**
     * 锁自动释放时间
     *
     * @return 自动释放时间
     */
    public long leaseTime() default 5L;

    /**
     * 锁自动释放时间单位
     *
     * @return 自动释放时间单位
     */
    public TimeUnit leaseTimeUnit() default TimeUnit.SECONDS;

}
