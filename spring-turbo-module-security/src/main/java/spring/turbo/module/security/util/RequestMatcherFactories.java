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

    /**
     * 返回或逻辑装饰器
     *
     * @param matchers 被装饰的其他匹配器
     * @return 装饰器
     * @see OrRequestMatcher
     */
    public static RequestMatcher or(RequestMatcher... matchers) {
        Asserts.notNull(matchers);
        Asserts.noNullElements(matchers);
        return new OrRequestMatcher(matchers);
    }

    /**
     * 返回与逻辑装饰器
     *
     * @param matchers 被装饰的其他匹配器
     * @return 装饰器
     * @see AndRequestMatcher
     */
    public static RequestMatcher and(RequestMatcher... matchers) {
        Asserts.notNull(matchers);
        Asserts.noNullElements(matchers);
        return new AndRequestMatcher(matchers);
    }

    /**
     * 返回非逻辑装饰器
     *
     * @param matcher 被装饰的其他匹配器
     * @return 装饰器
     * @see NegatedRequestMatcher
     */
    public static RequestMatcher not(RequestMatcher matcher) {
        Asserts.notNull(matcher);
        return new NegatedRequestMatcher(matcher);
    }

    /**
     * 返回匹配器匹配所有请求
     *
     * @return 匹配所有请求的匹配器
     * @see AnyRequestMatcher
     */
    public static RequestMatcher all() {
        return AnyRequestMatcher.INSTANCE;
    }

    /**
     * 返回一个匹配器不匹配任何请求
     *
     * @return 不匹配所有请求的匹配器
     */
    public static RequestMatcher none() {
        return request -> false;
    }

    // ------------------------------------------------------------------------------------------------------------------

    /**
     * 包装Predicate
     *
     * @param predicate {@link Predicate} 实例
     * @return {@link RequestMatcher} 实例
     * @see Predicate
     */
    public static RequestMatcher fromPredicate(Predicate<HttpServletRequest> predicate) {
        Asserts.notNull(predicate);
        return predicate::test;
    }

    // ------------------------------------------------------------------------------------------------------------------

    /**
     * 通过ANT-Style模式进行匹配
     *
     * @param pattern 模式
     * @return 匹配器实例
     * @see AntPathRequestMatcher
     */
    public static RequestMatcher antPath(String pattern) {
        Asserts.hasText(pattern);
        return new AntPathRequestMatcher(pattern);
    }

    /**
     * 通过ANT-Style模式进行匹配
     *
     * @param method  请求方法
     * @param pattern 模式
     * @return 匹配器实例
     * @see AntPathRequestMatcher
     */
    public static RequestMatcher antPath(HttpMethod method, String pattern) {
        return antPath(method, pattern, false);
    }

    /**
     * 通过ANT-Style模式进行匹配
     *
     * @param method        请求方法
     * @param pattern       模式
     * @param caseSensitive 是否大小写敏感
     * @return 匹配器实例
     * @see AntPathRequestMatcher
     */
    public static RequestMatcher antPath(HttpMethod method, String pattern, boolean caseSensitive) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new AntPathRequestMatcher(pattern, method.name(), caseSensitive);
    }

    /**
     * 通过IP地址匹配
     *
     * @param ipAddress 要匹配的IP地址
     * @return 匹配器实例
     * @see IpAddressMatcher
     */
    public static RequestMatcher ipAddress(String ipAddress) {
        Asserts.hasText(ipAddress);
        return new IpAddressMatcher(ipAddress);
    }

    /**
     * 通过MediaType匹配
     *
     * @param mediaTypes 要匹配的MediaType
     * @return 匹配器实例
     * @see MediaType
     * @see MediaTypeRequestMatcher
     */
    public static RequestMatcher mediaType(MediaType... mediaTypes) {
        Asserts.notNull(mediaTypes);
        Asserts.noNullElements(mediaTypes);
        return new MediaTypeRequestMatcher(mediaTypes);
    }

    /**
     * 通过DispatcherType匹配
     *
     * @param dispatcherType 要匹配的DispatcherType
     * @return 匹配器实例
     * @see DispatcherType
     * @see DispatcherTypeRequestMatcher
     */
    public static RequestMatcher dispatcherType(DispatcherType dispatcherType) {
        Asserts.notNull(dispatcherType);
        return new DispatcherTypeRequestMatcher(dispatcherType);
    }

    /**
     * 通过DispatcherType匹配
     *
     * @param dispatcherType 要匹配的DispatcherType
     * @param method         要匹配的方法
     * @return 匹配器实例
     * @see DispatcherType
     * @see DispatcherTypeRequestMatcher
     */
    public static RequestMatcher dispatcherType(DispatcherType dispatcherType, HttpMethod method) {
        Asserts.notNull(dispatcherType);
        Asserts.notNull(method);
        return new DispatcherTypeRequestMatcher(dispatcherType, method);
    }

    /**
     * 通过MVC模式匹配
     *
     * @param introspector {@link HandlerMappingIntrospector} 实例
     * @param pattern      要匹配的模式
     * @return 匹配器实例
     * @see HandlerMappingIntrospector
     * @see MvcRequestMatcher
     * @see MvcRequestMatcher.Builder
     */
    @Recommended
    public static RequestMatcher mvcPattern(HandlerMappingIntrospector introspector, String pattern) {
        Asserts.notNull(introspector);
        Asserts.hasText(pattern);
        return new MvcRequestMatcher.Builder(introspector)
                .pattern(pattern);
    }

    /**
     * 通过MVC模式匹配
     *
     * @param introspector {@link HandlerMappingIntrospector} 实例
     * @param method       要匹配的方法
     * @param pattern      要匹配的模式
     * @return 匹配器实例
     * @see HandlerMappingIntrospector
     * @see MvcRequestMatcher
     * @see MvcRequestMatcher.Builder
     */
    @Recommended
    public static RequestMatcher mvcPattern(HandlerMappingIntrospector introspector, HttpMethod method, String pattern) {
        Asserts.notNull(introspector);
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new MvcRequestMatcher.Builder(introspector)
                .pattern(method, pattern);
    }

    /**
     * 通过正则表达式匹配
     *
     * @param pattern 正则表达式
     * @return 匹配器实例
     * @see RegexRequestMatcher
     */
    public static RequestMatcher regexPattern(String pattern) {
        Asserts.hasText(pattern);
        return RegexRequestMatcher.regexMatcher(pattern);
    }

    /**
     * 通过正则表达式匹配
     *
     * @param pattern 正则表达式
     * @param method  要匹配的方法
     * @return 匹配器实例
     * @see RegexRequestMatcher
     */
    public static RequestMatcher regexPattern(String pattern, HttpMethod method) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new RegexRequestMatcher(pattern, method.name(), false);
    }

    /**
     * 通过正则表达式匹配
     *
     * @param pattern         正则表达式
     * @param method          要匹配的方法
     * @param caseInsensitive 是否大小写敏感
     * @return 匹配器实例
     * @see RegexRequestMatcher
     */
    public static RequestMatcher regexPattern(String pattern, HttpMethod method, boolean caseInsensitive) {
        Asserts.notNull(method);
        Asserts.hasText(pattern);
        return new RegexRequestMatcher(pattern, method.name(), caseInsensitive);
    }

    /**
     * 通过请求头匹配
     *
     * @param headerName 期望的请求头名
     * @param regex      正则表达式
     * @return 匹配器实例
     */
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

    /**
     * 通过参数匹配
     *
     * @param parameterName 期望的参数名
     * @param regex         正则表达式
     * @return 匹配器实例
     */
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
