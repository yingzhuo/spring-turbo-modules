/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Objects;

/**
 * {@link MustacheService} 默认实现类
 *
 * @author 应卓
 * @since 3.3.0
 */
public class MustacheServiceImpl implements MustacheService {

    private final MustacheFactory mustacheFactory;

    /**
     * 默认构造方法
     */
    public MustacheServiceImpl() {
        super();
        this.mustacheFactory = new DefaultMustacheFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(String templateString, String templateName, @Nullable Object module) {
        Asserts.hasText(templateName, "templateName is required");

        var writer = new StringWriter();
        var mustache = mustacheFactory.compile(new StringReader(templateString), templateName);
        mustache.execute(writer, Objects.requireNonNullElseGet(module, HashMap::new));
        return writer.toString();
    }

}
