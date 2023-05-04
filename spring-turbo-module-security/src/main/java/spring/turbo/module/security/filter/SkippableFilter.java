/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.function.Predicate;

import static spring.turbo.module.security.util.RequestMatcherFactories.alwaysFalse;
import static spring.turbo.module.security.util.RequestMatcherFactories.fromPredicate;

/**
 * 可跳过的过滤器
 *
 * @author 应卓
 *
 * @see jakarta.servlet.http.HttpServletRequest
 * @see spring.turbo.module.security.util.RequestMatcherFactories
 * @see Predicate
 *
 * @since 2.0.1
 */
public interface SkippableFilter extends Filter {

    public void setSkipRequestMatcher(@Nullable RequestMatcher skipRequestMatcher);

    public default void setSkipRequestPredicate(@Nullable Predicate<HttpServletRequest> predicate) {
        if (predicate != null) {
            setSkipRequestMatcher(fromPredicate(predicate));
        } else {
            setSkipRequestMatcher(alwaysFalse());
        }
    }

}
