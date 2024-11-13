package spring.turbo.module.webmvc.support.argument;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import spring.turbo.util.concurrent.ThreadSharedObjects;

/**
 * @author 应卓
 * @see SharedObject
 * @see ThreadSharedObjects
 * @since 2024-07-03
 */
@Deprecated(since = "3.4.0", forRemoval = true)
public class SharedObjectHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SharedObject.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        var annotation = parameter.getParameterAnnotation(SharedObject.class);

        if (annotation == null) {
            return null;
        }

        if (annotation.type() != void.class) {
            return ThreadSharedObjects.get(annotation.type());
        }

        if (!"".equals(annotation.name())) {
            return ThreadSharedObjects.get(annotation.name());
        }

        return null;
    }

}
