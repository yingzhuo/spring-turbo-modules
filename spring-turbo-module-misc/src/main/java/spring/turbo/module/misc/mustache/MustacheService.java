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

import static spring.turbo.util.CharsetPool.UTF_8;

/**
 * 集成 mustache.java 进行简易的模版渲染。
 *
 * @author 应卓
 * @see <a href="https://github.com/spullara/mustache.java">mustache.java官方文档</a>
 * @since 3.3.0
 */
public interface MustacheService {

    /**
     * 渲染文本
     *
     * @param classpathTemplateLocation 模板在classpath中的位置
     * @param data                      数据
     * @return 渲染结果
     */
    public String render(String classpathTemplateLocation, @Nullable Object data);

    /**
     * 渲染文本
     *
     * @param template     模板
     * @param templateName 模板名称
     * @param data         数据
     * @return 渲染结果
     */
    public default String render(Resource template, String templateName, @Nullable Object data) {
        return render(ResourceUtils.readText(template, UTF_8), templateName, data);
    }

    /**
     * 渲染文本
     *
     * @param templateString 字符串模版
     * @param templateName   模板名称
     * @param data           数据
     * @return 渲染结果
     */
    public String render(String templateString, String templateName, @Nullable Object data);

}
