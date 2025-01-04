package spring.turbo.module.jdbc.ds;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

@Aspect
public record DataSourceSwitchAdvice(int order) implements Ordered {

    @Around("@annotation(spring.turbo.module.jdbc.ds.DataSourceSwitch)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature methodSignature) {
            var annotation = methodSignature.getMethod().getAnnotation(DataSourceSwitch.class);
            var dataSourceName = annotation.value();
            RoutingDataSourceLookup.set(dataSourceName);
            try {
                return joinPoint.proceed();
            } finally {
                RoutingDataSourceLookup.remove();
            }
        } else {
            return joinPoint.proceed();
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}
