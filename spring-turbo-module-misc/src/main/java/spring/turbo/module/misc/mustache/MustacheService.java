/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.mustache;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import spring.turbo.io.ResourceUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author 应卓
 *
 * @since 3.3.0
 */
@FunctionalInterface
public interface MustacheService {

    public default String render(Resource template, @Nullable Object module) {
        return render(ResourceUtils.readText(template, StandardCharsets.UTF_8), module);
    }

    public String render(String templateString, @Nullable Object module);

}
