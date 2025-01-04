package spring.turbo.module.jdbc.ds;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import spring.turbo.core.AspectUtils;

/**
 * @author 应卓
 * @see DataSourceSwitch
 * @since 3.4.1
 */
@Aspect
public class DataSourceSwitchAdvice implements Ordered {

    private final int order;

    public DataSourceSwitchAdvice() {
        this(HIGHEST_PRECEDENCE);
    }

    public DataSourceSwitchAdvice(int order) {
        this.order = order;
    }

    @Around("@annotation(spring.turbo.module.jdbc.ds.DataSourceSwitch)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = AspectUtils.getMethodAnnotation(joinPoint, DataSourceSwitch.class);

        if (annotation == null) {
            annotation = AspectUtils.getObjectTypeAnnotation(joinPoint, DataSourceSwitch.class);
        }

        if (annotation == null) {
            return joinPoint.proceed();
        }

        try {
            RoutingDataSourceLookup.set(annotation.value());
            return joinPoint.proceed();
        } finally {
            RoutingDataSourceLookup.remove();
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}
