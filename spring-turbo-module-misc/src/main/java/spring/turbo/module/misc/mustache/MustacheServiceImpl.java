package spring.turbo.module.misc.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.resolver.ClasspathResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

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

        this.mustacheFactory = new DefaultMustacheFactory(new ClasspathResolver());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(String classpathTemplateLocation, @Nullable Object data) {
        Assert.hasText(classpathTemplateLocation, "classpathTemplateLocation is requuired");

        if (classpathTemplateLocation.startsWith("classpath:")) {
            classpathTemplateLocation = classpathTemplateLocation.substring("classpath:".length());
        }

        var writer = new StringWriter();
        var mustache = mustacheFactory.compile(classpathTemplateLocation);
        mustache.execute(writer, Objects.requireNonNullElseGet(data, HashMap::new));
        return writer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(String templateString, String templateName, @Nullable Object data) {
        Assert.hasText(templateName, "templateName is required");

        var writer = new StringWriter();
        var mustache = mustacheFactory.compile(new StringReader(templateString), templateName);
        mustache.execute(writer, Objects.requireNonNullElseGet(data, HashMap::new));
        return writer.toString();
    }

}
