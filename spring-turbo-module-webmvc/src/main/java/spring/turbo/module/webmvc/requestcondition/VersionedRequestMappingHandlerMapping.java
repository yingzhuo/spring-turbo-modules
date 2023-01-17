/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.requestcondition;

import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.module.webmvc.version.VersionResolver;
import spring.turbo.util.Asserts;

import java.lang.reflect.Method;

/**
 * @author 应卓
 * @since 2.0.9
 */
public class VersionedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final VersionResolver versionResolver;

    public VersionedRequestMappingHandlerMapping(VersionResolver versionResolver) {
        Asserts.notNull(versionResolver);
        this.versionResolver = versionResolver;
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        Versioned annotation = AnnotationUtils.findAnnotation(method, Versioned.class);
        if (annotation == null) {
            return super.getCustomMethodCondition(method);
        } else {
            return new VersionedRequestCondition(
                    this.versionResolver,
                    annotation.value(),
                    annotation.ignoreCase()
            );
        }
    }

}
