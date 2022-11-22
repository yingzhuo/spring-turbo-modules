/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.util;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.*;
import spring.turbo.util.Asserts;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 应卓
 * @see org.springframework.security.web.util.matcher.RequestMatcher
 * @since 1.3.1
 */
public final class RequestMatcherBuilder {

    public static RequestMatcherBuilder newInstance() {
        return new RequestMatcherBuilder();
    }

    private final List<RequestMatcher> matchers = new LinkedList<>();

    /**
     * 私有构造方法
     */
    private RequestMatcherBuilder() {
        super();
    }

    public RequestMatcherBuilder add(RequestMatcher matcher) {
        Asserts.notNull(matcher);
        this.matchers.add(matcher);
        return this;
    }

    public RequestMatcherBuilder addRevered(RequestMatcher matcher) {
        return add(new NegatedRequestMatcher(matcher));
    }

    public RequestMatcherBuilder ipAddress(String ipAddress) {
        return add(new IpAddressMatcher(ipAddress));
    }

    public RequestMatcherBuilder antPath(String pattern, HttpMethod httpMethod, boolean caseSensitive) {
        return add(new AntPathRequestMatcher(pattern, httpMethod.toString(), caseSensitive));
    }

    public RequestMatcherBuilder antPath(String pattern, HttpMethod httpMethod) {
        return add(new AntPathRequestMatcher(pattern, httpMethod.toString()));
    }

    public RequestMatcherBuilder antPath(String pattern) {
        return add(new AntPathRequestMatcher(pattern));
    }

    public RequestMatcherBuilder mediaType(MediaType... mediaTypes) {
        return add(new MediaTypeRequestMatcher(mediaTypes));
    }

    public RequestMatcherBuilder header(String headerName, String regex) {
        Asserts.notNull(headerName);
        Asserts.notNull(regex);
        matchers.add(request -> {
            final String headerValue = request.getHeader(headerName);
            if (headerValue == null) {
                return false;
            }
            return Pattern.matches(regex, headerValue);
        });
        return this;
    }

    public RequestMatcherBuilder query(String parameterName, String regex) {
        Asserts.notNull(parameterName);
        Asserts.notNull(regex);
        matchers.add(request -> {
            final String parameterValue = request.getParameter(parameterName);
            if (parameterValue == null) {
                return false;
            }
            return Pattern.matches(regex, parameterValue);
        });
        return this;
    }

    public RequestMatcher any() {
        if (matchers.size() == 0) {
            return request -> false;
        }
        if (matchers.size() == 1) {
            return matchers.get(0);
        } else {
            return new OrRequestMatcher(this.matchers);
        }
    }

    public RequestMatcher all() {
        if (matchers.size() == 0) {
            return request -> false;
        }
        if (matchers.size() == 1) {
            return matchers.get(0);
        } else {
            return new AndRequestMatcher(this.matchers);
        }
    }

    public RequestMatcher unique() {
        Asserts.isTrue(matchers.size() == 1);
        return matchers.get(0);
    }

}
