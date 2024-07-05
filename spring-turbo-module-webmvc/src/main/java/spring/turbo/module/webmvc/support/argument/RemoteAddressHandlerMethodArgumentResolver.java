package spring.turbo.module.webmvc.support.argument;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import spring.turbo.module.webmvc.util.RemoteAddressUtils;

import java.util.Optional;

/**
 * @author 应卓
 * @see RemoteAddress
 * @since 1.0.0
 */
public class RemoteAddressHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RemoteAddress.class) &&
                parameter.getParameterType() == String.class;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {

        try {
            return doResolveArgument(parameter, webRequest);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public Object doResolveArgument(MethodParameter parameter, NativeWebRequest webRequest) {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);

        String ip = null;

        if (request != null) {
            ip = RemoteAddressUtils.getIpAddress(request);
            if (!StringUtils.hasText(ip)) {
                ip = null;
            }
        }

        if (parameter.getParameterType() == String.class) {
            return ip;
        } else if (parameter.getParameterType() == Optional.class) {
            return Optional.ofNullable(ip);
        } else {
            return null;
        }
    }

}
