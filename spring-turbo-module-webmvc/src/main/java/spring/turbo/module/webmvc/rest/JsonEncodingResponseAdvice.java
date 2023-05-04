/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.api.Json;

import java.util.Objects;

/**
 * @author 应卓
 *
 * @since 1.2.2
 */
@RestControllerAdvice
public class JsonEncodingResponseAdvice implements ResponseBodyAdvice<Object> {

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 由于使用场景非常有限 从 2.1.0版本开始不再在
     * AutoConfig 中尝试自动配置 需要用到时，用户自行配置 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    private final JsonResponseEncoder encoder;
    private final ObjectMapper objectMapper;

    public JsonEncodingResponseAdvice(JsonResponseEncoder encoder) {
        this(encoder, new ObjectMapper());
    }

    public JsonEncodingResponseAdvice(JsonResponseEncoder encoder, @Nullable ObjectMapper objectMapper) {
        Asserts.notNull(encoder);
        this.encoder = encoder;
        this.objectMapper = Objects.requireNonNullElseGet(objectMapper, ObjectMapper::new);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType)
                && searchAnnotation(returnType) != null;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        final JsonResponseEncoding annotation = searchAnnotation(returnType);
        if (annotation == null || body == null) {
            return body;
        }

        if (!(body instanceof Json)) {
            return body;
        }

        return this.doEncode((Json) body);
    }

    @Nullable
    private JsonResponseEncoding searchAnnotation(MethodParameter returnType) {
        JsonResponseEncoding annotation = returnType.getMethodAnnotation(JsonResponseEncoding.class);

        if (annotation != null) {
            return annotation;
        }

        return returnType.getContainingClass().getAnnotation(JsonResponseEncoding.class);
    }

    private String doEncode(Json json) {
        try {
            return encoder.encode(this.objectMapper.writeValueAsString(json));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
