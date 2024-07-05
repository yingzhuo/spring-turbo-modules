package spring.turbo.module.webmvc.support.argument;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import spring.turbo.util.TreadSharedObjects;

/**
 * @author 应卓
 * @since 2024-07-03
 */
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

        if (annotation.byType() != void.class) {
            return TreadSharedObjects.get(annotation.byType());
        }

        if (!"".equals(annotation.byName())) {
            return TreadSharedObjects.get(annotation.byName());
        }

        return null;
    }

}
