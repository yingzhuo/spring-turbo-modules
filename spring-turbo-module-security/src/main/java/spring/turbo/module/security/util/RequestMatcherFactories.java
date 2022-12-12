/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.util;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.*;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author 应卓
 * @since 2.0.4
 */
public final class RequestMatcherFactories {

    /**
     * 私有构造方法
     */
    private RequestMatcherFactories() {
        super();
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher or(RequestMatcher... matchers) {
        return new OrRequestMatcher(matchers);
    }

    public static RequestMatcher and(RequestMatcher... matchers) {
        return new AndRequestMatcher(matchers);
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher all() {
        return AnyRequestMatcher.INSTANCE;
    }

    public static RequestMatcher none() {
        return request -> false;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher fromPredicate(Predicate<HttpServletRequest> predicate) {
        return predicate::test;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher antPath(String pattern) {
        return new AntPathRequestMatcher(pattern);
    }

    public static RequestMatcher antPath(HttpMethod method, String pattern) {
        return new AntPathRequestMatcher(pattern, method.name());
    }

    public static RequestMatcher antPath(HttpMethod method, String pattern, boolean caseSensitive) {
        return new AntPathRequestMatcher(pattern, method.name(), caseSensitive);
    }

    public static RequestMatcher ipAddress(String ipAddress) {
        return new IpAddressMatcher(ipAddress);
    }

    public static RequestMatcher mediaType(MediaType... mediaTypes) {
        return new MediaTypeRequestMatcher(mediaTypes);
    }

    public static RequestMatcher dispatcherType(DispatcherType dispatcherType) {
        return new DispatcherTypeRequestMatcher(dispatcherType);
    }

    public static RequestMatcher dispatcherType(DispatcherType dispatcherType, HttpMethod httpMethod) {
        return new DispatcherTypeRequestMatcher(dispatcherType, httpMethod);
    }

    public static RequestMatcher header(String headerName, String regex) {
        return request -> {
            final String headerValue = request.getHeader(headerName);
            if (headerValue == null) {
                return false;
            }
            return Pattern.matches(regex, headerValue);
        };
    }

    public static RequestMatcher query(String parameterName, String regex) {
        return request -> {
            final String parameterValue = request.getParameter(parameterName);
            if (parameterValue == null) {
                return false;
            }
            return Pattern.matches(regex, parameterValue);
        };
    }
}
