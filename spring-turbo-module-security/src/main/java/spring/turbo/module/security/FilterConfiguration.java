package spring.turbo.module.security;

import jakarta.servlet.Filter;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * {@link Filter} 配置单元
 * <p>
 * 本类用来配置SpringSecurity扩展过滤器，必须把此类的实现加入 {@link org.springframework.context.ApplicationContext}。
 *
 * @param <T> {@link Filter} 的类型
 * @author 应卓
 * @see org.springframework.security.web.SecurityFilterChain
 * @since 1.0.0
 */
@FunctionalInterface
public interface FilterConfiguration<T extends Filter> {

    @Nullable
    public T create();

    @Nullable
    public default Class<? extends Filter> positionInChain() {
        return BasicAuthenticationFilter.class;
    }

    /**
     * 需要配置的{@link Filter}在{@link org.springframework.security.web.SecurityFilterChain}中的位置
     */
    @Nullable
    public default Position position() {
        return Position.AFTER;
    }

    /**
     * 需要配置的{@link Filter}在{@link org.springframework.security.web.SecurityFilterChain}中的位置
     */
    public enum Position {
        BEFORE, AFTER, REPLACE
    }

    // -----------------------------------------------------------------------------------------------------------------

    record Default(Filter filter,
                   @Nullable Class<? extends Filter> positionInChain,
                   @Nullable Position position) implements FilterConfiguration<Filter> {

        @Override
        public Filter create() {
            return filter;
        }

        @Override
        public Class<? extends Filter> positionInChain() {
            return positionInChain;
        }

        @Override
        public Position position() {
            return this.position;
        }
    }

}
