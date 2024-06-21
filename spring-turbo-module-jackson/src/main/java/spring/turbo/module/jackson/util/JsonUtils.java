/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import spring.turbo.io.IOExceptionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import static spring.turbo.core.SpringUtils.getRequiredBean;

/**
 * @author 应卓
 * @since 3.3.1
 */
public final class JsonUtils {

    /**
     * 私有构造方法
     */
    private JsonUtils() {
        super();
    }

    public static String toJson(Object obj) {
        try {
            return getRequiredBean(ObjectMapper.class).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

    public static String toJsonWithoutIndent(Object obj) {
        try {
            return getRequiredBean(ObjectMapper.class)
                    .writer()
                    .withoutFeatures(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

    public static <T> T parseJson(String json, Class<T> objClass) {
        try {
            return getRequiredBean(ObjectMapper.class).readValue(json, objClass);
        } catch (JsonProcessingException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

    public static <T> T parseJson(String json, String jsonPath, final Class<T> objClass) {
        return parseJson(json, jsonPath, new TypeRef<T>() {
            @Override
            public Type getType() {
                return objClass;
            }
        });
    }

    public static <T> T parseJson(String json, String jsonPath, TypeRef<T> typeRef) {
        return JsonPath.using(getRequiredBean(Configuration.class)).parse(json).read(jsonPath, typeRef);
    }

    public static <T> T parseJson(InputStream json, Class<T> objClass) {
        try {
            return getRequiredBean(ObjectMapper.class).readValue(json, objClass);
        } catch (IOException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

    public static <T> T parseJson(InputStream json, String jsonPath, final Class<T> objClass) {
        return parseJson(json, jsonPath, new TypeRef<T>() {
            @Override
            public Type getType() {
                return objClass;
            }
        });
    }

    public static <T> T parseJson(InputStream json, String jsonPath, TypeRef<T> typeRef) {
        return JsonPath.using(getRequiredBean(Configuration.class)).parse(json).read(jsonPath, typeRef);
    }

    public static <T> T parseJson(Reader json, Class<T> objClass) {
        try {
            return getRequiredBean(ObjectMapper.class).readValue(json, objClass);
        } catch (IOException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

    public static <T> T parseJson(Reader json, String jsonPath, final Class<T> objClass) {
        return parseJson(json, jsonPath, new TypeRef<T>() {
            @Override
            public Type getType() {
                return objClass;
            }
        });
    }

    public static <T> T parseJson(Reader json, String jsonPath, TypeRef<T> typeRef) {
        return JsonPath.using(getRequiredBean(Configuration.class)).parse(json).read(jsonPath, typeRef);
    }
}
