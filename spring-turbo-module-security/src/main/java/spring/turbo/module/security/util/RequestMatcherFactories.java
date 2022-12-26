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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import spring.turbo.lang.Recommended;
import spring.turbo.util.Asserts;

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
        Asserts.notNull(matchers);
        Asserts.noNullElements(matchers);
        return new OrRequestMatcher(matchers);
    }

    public static RequestMatcher and(RequestMatcher... matchers) {
        Asserts.notNull(matchers);
        Asserts.noNullElements(matchers);
        return new AndRequestMatcher(matchers);
    }

    public static RequestMatcher not(RequestMatcher matcher) {
        Asserts.notNull(matcher);
        return new NegatedRequestMatcher(matcher);
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
        Asserts.notNull(predicate);
        return predicate::test;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher antPath(String pattern) {
        Asserts.hasText(pattern);
        return new AntPathRequestMatcher(pattern);
    }

    public static RequestMatcher antPath(HttpMethod method, String pattern) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new AntPathRequestMatcher(pattern, method.name());
    }

    public static RequestMatcher antPath(HttpMethod method, String pattern, boolean caseSensitive) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new AntPathRequestMatcher(pattern, method.name(), caseSensitive);
    }

    public static RequestMatcher ipAddress(String ipAddress) {
        Asserts.hasText(ipAddress);
        return new IpAddressMatcher(ipAddress);
    }

    public static RequestMatcher mediaType(MediaType... mediaTypes) {
        Asserts.notNull(mediaTypes);
        Asserts.noNullElements(mediaTypes);
        return new MediaTypeRequestMatcher(mediaTypes);
    }

    public static RequestMatcher dispatcherType(DispatcherType dispatcherType) {
        Asserts.notNull(dispatcherType);
        return new DispatcherTypeRequestMatcher(dispatcherType);
    }

    public static RequestMatcher dispatcherType(DispatcherType dispatcherType, HttpMethod method) {
        Asserts.notNull(dispatcherType);
        Asserts.notNull(method);
        return new DispatcherTypeRequestMatcher(dispatcherType, method);
    }

    @Recommended
    public static RequestMatcher mvcPattern(HandlerMappingIntrospector introspector, String pattern) {
        Asserts.notNull(introspector);
        Asserts.hasText(pattern);
        return new MvcRequestMatcher.Builder(introspector)
                .pattern(pattern);
    }

    @Recommended
    public static RequestMatcher mvcPattern(HandlerMappingIntrospector introspector, HttpMethod method, String pattern) {
        Asserts.notNull(introspector);
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new MvcRequestMatcher.Builder(introspector)
                .pattern(method, pattern);
    }

    public static RequestMatcher regexPattern(String pattern) {
        Asserts.hasText(pattern);
        return RegexRequestMatcher.regexMatcher(pattern);
    }

    public static RequestMatcher regexPattern(String pattern, HttpMethod method) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new RegexRequestMatcher(pattern, method.name(), false);
    }

    public static RequestMatcher regexPattern(String pattern, HttpMethod method, boolean caseInsensitive) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new RegexRequestMatcher(pattern, method.name(), caseInsensitive);
    }

    public static RequestMatcher header(String headerName, String regex) {
        Asserts.hasText(headerName);
        Asserts.hasText(regex);
        return request -> {
            final String headerValue = request.getHeader(headerName);
            if (headerValue == null) {
                return false;
            }
            return Pattern.matches(regex, headerValue);
        };
    }

    public static RequestMatcher query(String parameterName, String regex) {
        Asserts.hasText(parameterName);
        Asserts.hasText(regex);
        return request -> {
            final String parameterValue = request.getParameter(parameterName);
            if (parameterValue == null) {
                return false;
            }
            return Pattern.matches(regex, parameterValue);
        };
    }

}
