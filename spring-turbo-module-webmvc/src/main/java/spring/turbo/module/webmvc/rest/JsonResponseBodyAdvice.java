/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.rest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import spring.turbo.webmvc.api.Json;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.1.5
 */
public class JsonResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null || body instanceof Json) {
            return body;
        }

        if (body instanceof Map) {
            final Json json = Json.newInstance();
            for (Object key : ((Map<?, ?>) body).keySet()) {
                if (key != null) {
                    json.payload(key.toString(), ((Map<?, ?>) body).get(key));
                }
            }
            return json;
        }

        if ((body instanceof List) || (body instanceof Set) || body.getClass().isArray()) {
            final Json json = Json.newInstance();
            json.payload("list", body);
            return json;
        }

        return body;
    }

}
