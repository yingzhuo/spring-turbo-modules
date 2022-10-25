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
public class SecurityJsonResponseAdvice implements ResponseBodyAdvice<Object> {

    private final SecurityJsonDataEncoder encoder;
    private final ObjectMapper objectMapper;

    public SecurityJsonResponseAdvice(@Nullable SecurityJsonDataEncoder encoder) {
        this(encoder, null);
    }

    public SecurityJsonResponseAdvice(@Nullable SecurityJsonDataEncoder encoder, @Nullable ObjectMapper objectMapper) {
        this.encoder = encoder != null ? encoder : SecurityJsonDataEncoderFactories.noop();
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return findAnnotation(returnType) != null;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        final SecurityJsonData annotation = findAnnotation(returnType);
        if (annotation == null || body == null) {
            return body;
        }

        if (!(body instanceof Json)) {
            return body;
        }

        final String dataProperty = annotation.dataProperty();
        final Json json = (Json) body;
        final String payloadJsonString = getPayloadJsonString(json);
        json.getPayload().clear();
        json.payload(dataProperty, payloadJsonString);
        return json;
    }

    @Nullable
    private SecurityJsonData findAnnotation(MethodParameter returnType) {
        SecurityJsonData annotation = returnType.getMethodAnnotation(SecurityJsonData.class);
        if (annotation != null) {
            return annotation;
        }
        return returnType.getContainingClass().getAnnotation(SecurityJsonData.class);
    }

    private String getPayloadJsonString(Json json) {
        try {
            return encoder.encode(this.objectMapper.writeValueAsString(json.getPayload()));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
