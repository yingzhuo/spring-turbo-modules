/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.webmvc;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import spring.turbo.webmvc.RemoteAddressUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author 应卓
 * @see RemoteAddress
 * @since 1.0.0
 */
public class RemoteAddressHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean c1 = parameter.hasParameterAnnotation(RemoteAddress.class) && parameter.getParameterType() == String.class;
        boolean c2 = parameter.hasParameterAnnotation(RemoteAddress.class) && parameter.getParameterType() == Optional.class;
        return c1 || c2;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

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
