package spring.turbo.module.security.util;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import spring.turbo.util.collection.ArrayUtils;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.HttpMethod.GET;

/**
 * {@link RequestMatcher} 相关生成工具
 *
 * @author 应卓
 * @since 2.0.4
 */
public final class RequestMatcherFactories {

    /**
     * 私有构造方法
     */
    private RequestMatcherFactories() {
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher or(RequestMatcher... matchers) {
        return new OrRequestMatcher(matchers);
    }

    public static RequestMatcher and(RequestMatcher... matchers) {
        return new AndRequestMatcher(matchers);
    }

    public static RequestMatcher not(RequestMatcher matcher) {
        return new NegatedRequestMatcher(matcher);
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher alwaysTrue() {
        return AnyRequestMatcher.INSTANCE;
    }

    public static RequestMatcher alwaysFalse() {
        return request -> false;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher fromPredicate(Predicate<HttpServletRequest> predicate) {
        return predicate::test;
    }

    // ------------------------------------------------------------------------------------------------------------------

    public static RequestMatcher antPaths(HttpMethod method, String... patterns) {
        return antPaths(method, false, patterns);
    }

    public static RequestMatcher antPaths(HttpMethod method, boolean caseSensitive, String... patterns) {
        if (ArrayUtils.length(patterns) == 1) {
            return new AntPathRequestMatcher(patterns[0], method.name(), caseSensitive);
        } else {
            var list = new ArrayList<RequestMatcher>();
            for (var pattern : patterns) {
                list.add(new AntPathRequestMatcher(pattern, method.name(), caseSensitive));
            }
            return new OrRequestMatcher(list.toArray(new RequestMatcher[0]));
        }
    }

    public static RequestMatcher mvcPatterns(HandlerMappingIntrospector introspector, String... patterns) {
        if (ArrayUtils.length(patterns) == 1) {
            return new MvcRequestMatcher.Builder(introspector).pattern(patterns[0]);
        } else {
            var list = new ArrayList<RequestMatcher>();
            for (var pattern : patterns) {
                list.add(new MvcRequestMatcher.Builder(introspector).pattern(pattern));
            }
            return new OrRequestMatcher(list.toArray(new RequestMatcher[0]));
        }
    }

    public static RequestMatcher mvcPatterns(HandlerMappingIntrospector introspector, HttpMethod method,
                                             String... patterns) {
        if (ArrayUtils.length(patterns) == 1) {
            return new MvcRequestMatcher.Builder(introspector).pattern(method, patterns[0]);
        } else {
            var list = new ArrayList<RequestMatcher>();
            for (var pattern : patterns) {
                list.add(new MvcRequestMatcher.Builder(introspector).pattern(method, pattern));
            }
            return new OrRequestMatcher(list.toArray(new RequestMatcher[0]));
        }
    }

    public static RequestMatcher regexPatterns(String... patterns) {
        if (ArrayUtils.length(patterns) == 1) {
            return RegexRequestMatcher.regexMatcher(patterns[0]);
        } else {
            var list = new ArrayList<RequestMatcher>();
            for (var pattern : patterns) {
                list.add(RegexRequestMatcher.regexMatcher(pattern));
            }
            return new OrRequestMatcher(list.toArray(new RequestMatcher[0]));
        }
    }

    public static RequestMatcher regexPatterns(HttpMethod method, String... patterns) {
        return regexPatterns(method, false, patterns);
    }

    public static RequestMatcher regexPatterns(HttpMethod method, boolean caseInsensitive, String... patterns) {
        if (ArrayUtils.length(patterns) == 1) {
            return new RegexRequestMatcher(patterns[0], method.name(), caseInsensitive);
        } else {
            var list = new ArrayList<RequestMatcher>();
            for (var pattern : patterns) {
                list.add(new RegexRequestMatcher(pattern, method.name(), caseInsensitive));
            }
            return new OrRequestMatcher(list.toArray(new RequestMatcher[0]));
        }
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

    public static RequestMatcher dispatcherType(DispatcherType dispatcherType, HttpMethod method) {
        return new DispatcherTypeRequestMatcher(dispatcherType, method);
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

    public static RequestMatcher isSecure() {
        return ServletRequest::isSecure;
    }

    public static RequestMatcher isNotSecure() {
        return request -> !request.isSecure();
    }

    public static RequestMatcher defaultKubernetesProps(HandlerMappingIntrospector introspector) {
        return kubernetesProps(introspector, GET, "/actuator", "/actuator/*", "/actuator/*/*");
    }

    public static RequestMatcher kubernetesProps(HandlerMappingIntrospector introspector, HttpMethod method,
                                                 String... patterns) {
        return and(mvcPatterns(introspector, method, patterns), header(USER_AGENT, "^.*kube-probe/.*$"));
    }

}
