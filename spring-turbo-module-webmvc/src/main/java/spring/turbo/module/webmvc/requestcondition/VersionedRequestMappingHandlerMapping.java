/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.requestcondition;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import spring.turbo.module.webmvc.version.ServletPathVersionResolver;
import spring.turbo.module.webmvc.version.VersionResolver;

import java.lang.reflect.Method;

import static java.util.Objects.requireNonNullElseGet;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;

/**
 * @author 应卓
 * @see ServletPathVersionResolver
 * @since 2.0.9
 */
public class VersionedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * 本类型不会被本starter自动注册
     * 用户必须自行注册
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private final VersionResolver versionResolver;

    /**
     * 默认构造方法
     */
    public VersionedRequestMappingHandlerMapping() {
        this(new ServletPathVersionResolver());
    }

    /**
     * 构造方法
     *
     * @param versionResolver 版本号解析器
     */
    public VersionedRequestMappingHandlerMapping(@Nullable VersionResolver versionResolver) {
        this.versionResolver = requireNonNullElseGet(versionResolver, ServletPathVersionResolver::new);
    }

    @Nullable
    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        var annotation = findAnnotation(method, Versioned.class);

        if (annotation == null) {
            return null;
        }

        var annotationAttributes = getAnnotationAttributes(annotation, false, false);

        return new VersionedRequestCondition(
                versionResolver,
                annotationAttributes.getString("value"),
                annotationAttributes.getBoolean("ignoreCase")
        );
    }

}
