package spring.turbo.module.security.token;

import org.springframework.core.OrderComparator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;
import spring.turbo.util.collection.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 组合型令牌解析器
 * <p>
 * 本类型解析器封装多个其他的解析器，如果之前的解析器不能解析出令牌，则尝试下一个。
 *
 * @author 应卓
 * @since 1.0.0
 */
public final class CompositeTokenResolver implements TokenResolver {

    private final List<TokenResolver> resolvers = new ArrayList<>();

    public CompositeTokenResolver(TokenResolver... resolvers) {
        // 添加并排序
        CollectionUtils.nullSafeAddAll(this.resolvers, resolvers);
        OrderComparator.sort(this.resolvers);
    }

    public CompositeTokenResolver(Collection<TokenResolver> resolvers) {
        // 添加并排序
        CollectionUtils.nullSafeAddAll(this.resolvers, resolvers);
        OrderComparator.sort(this.resolvers);
    }

    public static CompositeTokenResolver of(TokenResolver... resolvers) {
        return new CompositeTokenResolver(resolvers);
    }

    @NonNull
    @Override
    public Optional<Token> resolve(WebRequest request) {
        for (TokenResolver it : resolvers) {
            Optional<Token> op = doResolve(it, request);
            if (op.isPresent())
                return op;
        }
        return Optional.empty();
    }

    // since 1.0.5
    private Optional<Token> doResolve(@Nullable TokenResolver resolver, WebRequest request) {
        try {
            if (resolver != null) {
                return resolver.resolve(request);
            } else {
                return Optional.empty();
            }
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }

}
