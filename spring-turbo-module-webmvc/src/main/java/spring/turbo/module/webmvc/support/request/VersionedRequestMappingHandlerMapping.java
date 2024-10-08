package spring.turbo.module.webmvc.support.request;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import spring.turbo.module.webmvc.util.version.CompositeVersionResolver;
import spring.turbo.module.webmvc.util.version.ServletPathVersionResolver;
import spring.turbo.module.webmvc.util.version.VersionResolver;

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

    /*
     * 本组件不会被本starter自动注册 用户必须自行注册
     */

    private final VersionResolver versionResolver;

    /**
     * 默认构造方法
     */
    public VersionedRequestMappingHandlerMapping() {
        this(null);
    }

    /**
     * 构造方法
     *
     * @param versionResolver 版本号解析器
     */
    public VersionedRequestMappingHandlerMapping(@Nullable VersionResolver versionResolver) {
        this.versionResolver = requireNonNullElseGet(versionResolver, CompositeVersionResolver::getDefault);
    }

    @Nullable
    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        var annotation = findAnnotation(method, Versioned.class);

        if (annotation == null) {
            return null;
        }

        var annotationAttributes = getAnnotationAttributes(annotation, false, false);

        return new VersionedRequestCondition(versionResolver, annotationAttributes.getString("value"),
                annotationAttributes.getBoolean("ignoreCase"));
    }

}
