package spring.turbo.module.jackson.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import spring.turbo.core.SpringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Objects;

import static spring.turbo.util.io.IOExceptionUtils.toUnchecked;

/**
 * Json相关工具
 *
 * @author 应卓
 * @see <a href="https://github.com/FasterXML/jackson">Jackson官方文档</a>
 * @see <a href="https://github.com/json-path/JsonPath">JsonPath官方文档</a>
 * @see ObjectMapper
 * @see JsonMapper
 * @since 3.3.1
 */
public final class JsonUtils {

    /**
     * 私有构造方法
     */
    private JsonUtils() {
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
                    .withoutFeatures(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 序列化
     * <ul>
     *     <li>缩进</li>
     * </ul>
     *
     * @param obj 要序列化的对象
     * @return json
     * @throws java.io.UncheckedIOException 处理失败
     * @see com.fasterxml.jackson.databind.SerializationFeature#INDENT_OUTPUT
     */
    public static String toJsonWithIndent(Object obj) {
        try {
            return getObjectMapper()
                    .writer()
                    .withFeatures(SerializationFeature.INDENT_OUTPUT)
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
                    .withoutFeatures(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw toUnchecked(e);
        }
    }

    /**
     * 序列化
     * <ul>
     *     <li>指定视图</li>
     *     <li>缩进</li>
     * </ul>
     *
     * @param obj       要序列化的对象
     * @param viewClass 要混入的视图类
     * @return json
     * @throws java.io.UncheckedIOException 处理失败
     * @see com.fasterxml.jackson.databind.SerializationFeature#INDENT_OUTPUT
     */
    public static String toJsonWithViewWithIndent(Object obj, Class<?> viewClass) {
        try {
            return getObjectMapper()
                    .writer()
                    .withView(viewClass)
                    .withFeatures(SerializationFeature.INDENT_OUTPUT)
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
        return parseJson(json, jsonPath, SimpleTypeRef.of(objClass));
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
        return JsonPath.using(getJsonPathConf())
                .parse(json)
                .read(jsonPath, typeRef);
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
    public static <T> T parseJson(InputStream json, String jsonPath, Class<T> objClass) {
        return parseJson(json, jsonPath, SimpleTypeRef.of(objClass));
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
        return JsonPath.using(getJsonPathConf())
                .parse(json)
                .read(jsonPath, typeRef);
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
    public static <T> T parseJson(Reader json, String jsonPath, Class<T> objClass) {
        return parseJson(json, jsonPath, SimpleTypeRef.of(objClass));
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

    public static ObjectMapper getObjectMapper() {
        return SpringUtils.getBean(ObjectMapper.class)
                .orElseGet(SyncAvoid::getJsonMapper);
    }

    public static Configuration getJsonPathConf() {
        return SpringUtils.getBean(Configuration.class)
                .orElseGet(SyncAvoid::getJsonPathConf);
    }

    // -----------------------------------------------------------------------------------------------------------------

    // 延迟加载
    @SuppressWarnings("deprecation")
    private static class SyncAvoid {

        private static final JsonMapper JSON_MAPPER;
        private static final Configuration JSON_PATH_CONF;

        static {
            // 以下都是作者喜欢的配置，你如果不喜欢，作者表示那没什么好办法满足你。
            JSON_MAPPER = JsonMapper.builder()
                    .configure(SerializationFeature.INDENT_OUTPUT, true)
                    .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true)
                    .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true)
                    .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
                    .configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false)
                    .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                    .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                    .build();

            // 尝试注册能发现的所有模块
            JacksonModuleUtils.loadAndRegisterModules(JSON_MAPPER, Objects::nonNull);

            JSON_PATH_CONF = Configuration.builder()
                    .jsonProvider(new JacksonJsonProvider(JSON_MAPPER))
                    .mappingProvider(new JacksonMappingProvider(JSON_MAPPER))
                    .build();
        }

        private static ObjectMapper getJsonMapper() {
            return JSON_MAPPER;
        }

        private static Configuration getJsonPathConf() {
            return JSON_PATH_CONF;
        }
    }

}
