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
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import spring.turbo.webmvc.api.Json;

/**
 * @author 应卓
 * @since 1.2.2
 */
@RestControllerAdvice
public class JsonEncodingResponseAdvice implements ResponseBodyAdvice<Object> {

    private final JsonEncoder encoder;
    private final ObjectMapper objectMapper;

    public JsonEncodingResponseAdvice(@Nullable JsonEncoder encoder) {
        this(encoder, null);
    }

    public JsonEncodingResponseAdvice(@Nullable JsonEncoder encoder, @Nullable ObjectMapper objectMapper) {
        this.encoder = encoder != null ? encoder : JsonEncoderFactories.noop();
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return searchAnnotation(returnType) != null;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        final JsonEncoding annotation = searchAnnotation(returnType);
        if (annotation == null || body == null) {
            return body;
        }

        if (!(body instanceof Json)) {
            return body;
        }

        return this.doEncode((Json) body);
    }

    @Nullable
    private JsonEncoding searchAnnotation(MethodParameter returnType) {
        JsonEncoding annotation = returnType.getMethodAnnotation(JsonEncoding.class);

        if (annotation != null) {
            return annotation;
        }

        return returnType.getContainingClass()
                .getAnnotation(JsonEncoding.class);
    }

    private String doEncode(Json json) {
        try {
            return encoder.encode(this.objectMapper.writeValueAsString(json));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
