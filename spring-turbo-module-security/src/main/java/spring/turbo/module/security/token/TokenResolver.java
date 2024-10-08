package spring.turbo.module.security.token;

import org.springframework.core.Ordered;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

/**
 * 令牌解析器 从{@code HTTP}请求中获取令牌
 *
 * @author 应卓
 * @see #builder()
 * @see TokenResolverBuilder
 * @see CompositeTokenResolver
 * @since 1.0.0
 */
@FunctionalInterface
public interface TokenResolver extends Ordered {

    /**
     * 新建创建器
     *
     * @return 创建器
     */
    public static TokenResolverBuilder builder() {
        return new TokenResolverBuilder();
    }

    /**
     * 解析令牌
     *
     * @param request HTTP请求
     * @return 令牌Optional，不能成功解析时返回empty-optional
     */
    public Optional<Token> resolve(WebRequest request);

    /**
     * 获取排序值
     * <p>
     * 多个令牌解析器同时作用时，可自由指定顺序。排序值越大，排序越靠后。
     *
     * @return 排序值
     * @see CompositeTokenResolver
     * @see Ordered#getOrder()
     * @see Ordered#LOWEST_PRECEDENCE
     * @see Ordered#HIGHEST_PRECEDENCE
     * @see org.springframework.core.OrderComparator
     */
    @Override
    public default int getOrder() {
        return 0;
    }

}
