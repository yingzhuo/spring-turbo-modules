/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.sql;

import org.springframework.util.StringUtils;
import spring.turbo.util.ClassUtils;

import static spring.turbo.util.StringPool.SLASH;

/**
 * (内部工具)
 *
 * @author 应卓
 *
 * @since 2.0.11
 */
class ClassPathDirUtils {

    public static String getClassPathDir(Class<?> clz) {
        return getClassPathDir(clz, true, true);
    }

    public static String getClassPathDir(Class<?> clz, boolean forceStartsWithSlash, boolean forceEndsWithSlash) {
        String path = ClassUtils.getPackageName(clz).replaceAll("\\.", SLASH);
        if (forceStartsWithSlash && !StringUtils.startsWithIgnoreCase(path, SLASH)) {
            path = SLASH + path;
        }

        if (forceEndsWithSlash && !StringUtils.endsWithIgnoreCase(path, SLASH)) {
            path += SLASH;
        }
        return path;
    }

}
