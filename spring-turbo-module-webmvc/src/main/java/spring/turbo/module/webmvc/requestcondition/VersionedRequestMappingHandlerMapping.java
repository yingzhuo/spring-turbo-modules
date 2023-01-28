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
import spring.turbo.core.AnnotationUtils;
import spring.turbo.module.webmvc.version.ServletPathVersionResolver;
import spring.turbo.module.webmvc.version.VersionResolver;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author 应卓
 * @since 2.0.9
 */
public class VersionedRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * 本类型不会被本starter自动注册
     * 用户必须自行注册
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private final VersionResolver versionResolver;

    public VersionedRequestMappingHandlerMapping() {
        this(null);
    }

    public VersionedRequestMappingHandlerMapping(@Nullable VersionResolver versionResolver) {
        versionResolver = Objects.requireNonNullElse(versionResolver, new ServletPathVersionResolver());
        this.versionResolver = versionResolver;
    }

    @Nullable
    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        final var annotation = AnnotationUtils.findAnnotation(method, Versioned.class);

        if (annotation == null) {
            return null;
        } else {
            return new VersionedRequestCondition(
                    this.versionResolver,
                    annotation.value(),
                    annotation.ignoreCase()
            );
        }
    }

}
