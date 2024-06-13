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
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.lang.Nullable;
import spring.turbo.io.IOExceptionUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;

import static spring.turbo.util.RandomStringUtils.randomAlphabetic;

/**
 * @author 应卓
 *
 * @since 3.3.0
 */
public class MustacheServiceImpl implements MustacheService {

    @Override
    public String render(String templateString, @Nullable Object module) {
        try {
            Writer writer = new StringWriter();
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader(templateString), randomAlphabetic(10));
            mustache.execute(writer, Objects.requireNonNullElseGet(module, Object::new));
            writer.flush();
            return writer.toString();
        } catch (IOException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

}
