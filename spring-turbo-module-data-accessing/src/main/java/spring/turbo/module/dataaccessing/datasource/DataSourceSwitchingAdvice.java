package spring.turbo.module.dataaccessing.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import spring.turbo.aop.AspectUtils;

/**
 * 动态数据源切换用切面
 *
 * @author 应卓
 * @see DataSourceSwitch
 * @since 1.1.0
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceSwitchingAdvice {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSwitchingAdvice.class);

    @Pointcut("@annotation(spring.turbo.module.dataaccessing.datasource.DataSourceSwitch)")
    public void annotationPointcut() {
    }

    @Pointcut("execution(@(@spring.turbo.module.dataaccessing.datasource.DataSourceSwitch *) * *(..))")
    public void inheritedAnnotationPointcut() {
    }

    @Around("annotationPointcut() || inheritedAnnotationPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var annotation = AspectUtils.getMethodAnnotation(joinPoint, DataSourceSwitch.class);

        if (annotation != null) {
            final String key = annotation.value();
            log.info("switch datasource to '{}'", key);
            DynamicDataSourceRemote.setKey(key);
        }

        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceRemote.clear();
        }
    }

}
