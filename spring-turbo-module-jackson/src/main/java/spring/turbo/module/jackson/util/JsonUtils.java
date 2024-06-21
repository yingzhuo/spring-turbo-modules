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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static spring.turbo.core.SpringUtils.getRequiredBean;
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
            return getRequiredBean(ObjectMapper.class).writeValueAsString(obj);
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
     */
    public static String toJsonWithoutIndent(Object obj) {
        try {
            return getRequiredBean(ObjectMapper.class)
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
            return getRequiredBean(ObjectMapper.class)
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
     */
    public static String toJsonWithViewWithoutIndent(Object obj, Class<?> viewClass) {
        try {
            return getRequiredBean(ObjectMapper.class)
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
            return getRequiredBean(ObjectMapper.class).readValue(json, objClass);
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
        return JsonPath.using(getRequiredBean(Configuration.class)).parse(json).read(jsonPath, typeRef);
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
            return getRequiredBean(ObjectMapper.class).readValue(json, objClass);
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
        return JsonPath.using(getRequiredBean(Configuration.class)).parse(json).read(jsonPath, typeRef);
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
            return getRequiredBean(ObjectMapper.class).readValue(json, objClass);
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
        return JsonPath.using(getRequiredBean(Configuration.class)).parse(json).read(jsonPath, typeRef);
    }

}
