package spring.turbo.module.redis.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import spring.turbo.core.AspectUtils;
import spring.turbo.core.SpELForAspectAround;
import spring.turbo.exception.RuntimeExceptionSupplier;

/**
 * @author 应卓
 * @see AvoidRepeatedInvocation
 * @since 3.4.0
 */
@Aspect
public class AvoidRepeatedInvocationAdvice implements Ordered {

    private final RedisOperations<String, String> redisOperations;
    private final RuntimeExceptionSupplier exceptionSupplier;
    private final int order;

    /**
     * 构造方法
     *
     * @param redisOperations   RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param exceptionSupplier 异常提供器，如果判断为重复调用。使用这个东西产生异常并抛出
     */
    public AvoidRepeatedInvocationAdvice(RedisOperations<String, String> redisOperations, RuntimeExceptionSupplier exceptionSupplier) {
        this(redisOperations, exceptionSupplier, HIGHEST_PRECEDENCE);
    }

    /**
     * 构造方法
     *
     * @param redisOperations   RedisOperations实例，通常是 {@link StringRedisTemplate}
     * @param exceptionSupplier 异常提供器，如果判断为重复调用。使用这个东西产生异常并抛出
     * @param order             切面排序
     */
    public AvoidRepeatedInvocationAdvice(RedisOperations<String, String> redisOperations, RuntimeExceptionSupplier exceptionSupplier, int order) {
        Assert.notNull(redisOperations, "redisOperations is required");
        Assert.notNull(exceptionSupplier, "exceptionSupplier is required");

        this.redisOperations = redisOperations;
        this.exceptionSupplier = exceptionSupplier;
        this.order = order;
    }

    @Around("@annotation(spring.turbo.module.redis.aspect.AvoidRepeatedInvocation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = AspectUtils.getMethodAnnotation(joinPoint, AvoidRepeatedInvocation.class);
        if (annotation == null) {
            return joinPoint.proceed();
        }

        var redisKey = SpELForAspectAround.newInstance(annotation.value(), joinPoint)
                .getValue();

        var success = redisOperations.opsForValue()
                .setIfAbsent(redisKey, "1", annotation.leaseTime(), annotation.leaseTimeUnit());

        if (success) {
            return joinPoint.proceed();
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public int getOrder() {
        return this.order;
    }

}
