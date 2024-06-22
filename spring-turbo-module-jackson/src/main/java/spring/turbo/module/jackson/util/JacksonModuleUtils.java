/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.util;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;
import spring.turbo.util.ServiceLoaderUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Jackson模块加载工具
 *
 * @author 应卓
 * @see Module
 * @see <a href="https://github.com/FasterXML/jackson">Jackson官方文档</a>
 * @since 3.3.1
 */
public final class JacksonModuleUtils {

    /**
     * 私有构造方法
     */
    private JacksonModuleUtils() {
        super();
    }

    /**
     * 加载classpath中被声明的所有的模块
     *
     * @return 模块(多个)
     */
    public static List<Module> loadModules() {
        return loadModules(null);
    }

    /**
     * 加载classpath中被声明的所有的模块
     *
     * @param predicate 过滤用predicate
     * @return 模块(多个)
     */
    public static List<Module> loadModules(@Nullable Predicate<Module> predicate) {
        return ServiceLoaderUtils.loadQuietly(Module.class)
                .stream()
                .filter(Objects.requireNonNullElse(predicate, Objects::nonNull))
                .toList();
    }

    /**
     * 加载classpath中被声明的所有的模块，并注册到 {@link ObjectMapper} 实例。
     *
     * @param objectMapper 要注册的{@link ObjectMapper} 实例
     */
    public static void loadAndRegisterModules(ObjectMapper objectMapper) {
        loadAndRegisterModules(objectMapper, null);
    }

    /**
     * 加载classpath中被声明模块，并注册到 {@link ObjectMapper} 实例。
     *
     * @param objectMapper 要注册的{@link ObjectMapper} 实例
     * @param predicate    过滤用predicate
     */
    public static void loadAndRegisterModules(ObjectMapper objectMapper, @Nullable Predicate<Module> predicate) {
        Asserts.notNull(objectMapper);
        objectMapper.registerModules(loadModules(predicate));
    }

}
