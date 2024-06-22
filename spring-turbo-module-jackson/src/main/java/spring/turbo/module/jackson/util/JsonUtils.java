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
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import spring.turbo.core.SpringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static spring.turbo.io.IOExceptionUtils.toUnchecked;

/**
 * Json相关工具
 *
 * @author 应卓
 * @see <a href="https://github.com/FasterXML/jackson">Jackson官方文档</a>
 * @see <a href="https://github.com/json-path/JsonPath">JsonPath官方文档</a>
 * @see ObjectMapper
 * @since 3.3.1
 */
public final class JsonUtils {

    /**
     * 私有构造方法
     */
    private JsonUtils() {
        super();
    }

    /**
     * 序列化
     *
     * @param obj 要序列化的对象
     * @return json
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static String toJson(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 序列化
     * <ul>
     *     <li>不要缩进</li>
     * </ul>
     *
     * @param obj 要序列化的对象
     * @return json
     * @throws java.io.UncheckedIOException 处理失败
     * @see com.fasterxml.jackson.databind.SerializationFeature#INDENT_OUTPUT
     */
    public static String toJsonWithoutIndent(Object obj) {
        try {
            return getObjectMapper()
                    .writer()
                    .withoutFeatures(INDENT_OUTPUT)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 序列化
     * <ul>
     *     <li>指定视图</li>
     * </ul>
     *
     * @param obj       要序列化的对象
     * @param viewClass 要混入的视图类
     * @return json
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static String toJsonWithView(Object obj, Class<?> viewClass) {
        try {
            return getObjectMapper()
                    .writer()
                    .withView(viewClass)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 序列化
     * <ul>
     *     <li>指定视图</li>
     *     <li>不要缩进</li>
     * </ul>
     *
     * @param obj       要序列化的对象
     * @param viewClass 要混入的视图类
     * @return json
     * @throws java.io.UncheckedIOException 处理失败
     * @see com.fasterxml.jackson.databind.SerializationFeature#INDENT_OUTPUT
     */
    public static String toJsonWithViewWithoutIndent(Object obj, Class<?> viewClass) {
        try {
            return getObjectMapper()
                    .writer()
                    .withView(viewClass)
                    .withoutFeatures(INDENT_OUTPUT)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param objClass 要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(String json, Class<T> objClass) {
        try {
            return getObjectMapper().readValue(json, objClass);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param jsonPath json path
     * @param objClass 要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(String json, String jsonPath, Class<T> objClass) {
        return parseJson(json, jsonPath, new TypeRef<T>() {
            @Override
            public Type getType() {
                return objClass;
            }
        });
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param jsonPath json path
     * @param typeRef  要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(String json, String jsonPath, TypeRef<T> typeRef) {
        return JsonPath.using(getJsonPathConf()).parse(json).read(jsonPath, typeRef);
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param objClass 要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(InputStream json, Class<T> objClass) {
        try {
            return getObjectMapper().readValue(json, objClass);
        } catch (IOException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param jsonPath json path
     * @param objClass 要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(InputStream json, String jsonPath, final Class<T> objClass) {
        return parseJson(json, jsonPath, new TypeRef<T>() {
            @Override
            public Type getType() {
                return objClass;
            }
        });
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param jsonPath json path
     * @param typeRef  要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(InputStream json, String jsonPath, TypeRef<T> typeRef) {
        return JsonPath.using(getJsonPathConf()).parse(json).read(jsonPath, typeRef);
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param objClass 要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(Reader json, Class<T> objClass) {
        try {
            return getObjectMapper().readValue(json, objClass);
        } catch (IOException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param jsonPath json path
     * @param objClass 要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(Reader json, String jsonPath, final Class<T> objClass) {
        return parseJson(json, jsonPath, new TypeRef<T>() {
            @Override
            public Type getType() {
                return objClass;
            }
        });
    }

    /**
     * 反序列化
     *
     * @param json     json
     * @param jsonPath json path
     * @param typeRef  要转换的类型
     * @param <T>      要转换的类型泛型
     * @return 实例
     * @throws java.io.UncheckedIOException 处理失败
     */
    public static <T> T parseJson(Reader json, String jsonPath, TypeRef<T> typeRef) {
        return JsonPath.using(getJsonPathConf())
                .parse(json)
                .read(jsonPath, typeRef);
    }

    private static ObjectMapper getObjectMapper() {
        return SpringUtils.getBean(ObjectMapper.class)
                .orElseGet(ObjectMapperSyncAvoid::get);
    }

    private static Configuration getJsonPathConf() {
        return SpringUtils.getBean(Configuration.class)
                .orElseGet(JsonPathConfSyncAvoid::get);
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    private static class ObjectMapperSyncAvoid {

        private static ObjectMapper get() {
            return OBJECT_MAPPER;
        }

        private static final ObjectMapper OBJECT_MAPPER;

        static {
            OBJECT_MAPPER = new ObjectMapper();
            try {
                JacksonModuleUtils.loadAndRegisterModules(OBJECT_MAPPER);
            } catch (Exception ignored) {
                // noop
            }
        }
    }

    // 延迟加载
    private static class JsonPathConfSyncAvoid {

        private static Configuration get() {
            return JSON_PATH_CONF;
        }

        private static final Configuration JSON_PATH_CONF;

        static {
            JSON_PATH_CONF = Configuration.builder()
                    .jsonProvider(new JacksonJsonProvider(ObjectMapperSyncAvoid.get()))
                    .mappingProvider(new JacksonMappingProvider(ObjectMapperSyncAvoid.get()))
                    .build();
        }
    }

}
