/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.javassist.classcache;

import org.springframework.lang.Nullable;
import spring.turbo.lang.Mutable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 应卓
 * @since 1.2.2
 */
@Mutable
public class ClassCache extends ConcurrentHashMap<String, Class<?>> {

    public void set(@Nullable Class<?> clz) {
        if (clz != null) {
            super.put(clz.getName(), clz);
        }
    }

    @Nullable
    public Class<?> find(String name) {
        return super.getOrDefault(name, null);
    }

}
