package spring.turbo.module.security.token;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;
import spring.turbo.util.StringUtils;

import java.util.Optional;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * 从HTTP QUERY中解析令牌
 *
 * @author 应卓
 * @since 1.0.0
 */
public class QueryTokenResolver implements TokenResolver {

    protected final String paramName;
    protected final String prefix;
    protected final int prefixLen;

    /**
     * 构造方法
     *
     * @param paramName query name
     */
    public QueryTokenResolver(@NonNull String paramName) {
        this(paramName, EMPTY);
    }

    /**
     * 构造方法
     *
     * @param paramName query name
     * @param prefix    前缀
     */
    public QueryTokenResolver(@NonNull String paramName, @Nullable String prefix) {
        if (prefix == null)
            prefix = EMPTY;
        this.paramName = paramName;
        this.prefix = prefix;
        this.prefixLen = prefix.length();
    }

    /**
     * 解析令牌
     *
     * @param request HTTP请求
     * @return 令牌Optional，不能成功解析时返回empty-optional
     */
    @NonNull
    @Override
    public Optional<Token> resolve(WebRequest request) {
        String paramValue = request.getParameter(paramName);

        if (paramValue == null || !paramValue.startsWith(prefix)) {
            return Optional.empty();
        }

        paramValue = paramValue.substring(prefixLen);

        if (StringUtils.isBlank(paramValue)) {
            return Optional.empty();
        }

        return Optional.of(StringToken.of(paramValue));
    }

    /**
     * 排序参数
     *
     * @return 排序值
     */
    @Override
    public int getOrder() {
        return -100;
    }

    public String getParamName() {
        return paramName;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPrefixLen() {
        return prefixLen;
    }

}
