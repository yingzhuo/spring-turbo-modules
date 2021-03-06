/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import spring.turbo.core.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 动态数据源切换用切面
 *
 * @author 应卓
 * @since 1.1.0
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceSwitchingAdvice {

    private static final Class<DataSourceSwitch> ANNOTATION_TYPE = DataSourceSwitch.class;

    @Around("@annotation(spring.turbo.module.datasource.DataSourceSwitch)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final DataSourceSwitch annotation = AnnotationUtils.findAnnotation(method, ANNOTATION_TYPE);

        if (annotation != null) {
            final String key = annotation.value();
            log.info("switch datasource to '{}'", key);
            DynamicDataSourceRemote.setKey(key);
        } else {
            log.info("switch datasource to default");
        }

        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceRemote.clear();
        }
    }

}
