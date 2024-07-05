package spring.turbo.module.configuration.env;

import org.springframework.core.env.PropertySource;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * @author 应卓
 * @since 2.1.3
 */
public final class EmptyPropertySource extends PropertySource<Object> {

    /**
     * 私有构造方法
     *
     * @param name 名称
     */
    private EmptyPropertySource(String name) {
        super(name);
    }

    public static EmptyPropertySource of(@Nullable String name) {
        return new EmptyPropertySource(Objects.requireNonNullElse(name, "empty"));
    }

    @Nullable
    @Override
    public Object getProperty(String name) {
        return null;
    }

}
