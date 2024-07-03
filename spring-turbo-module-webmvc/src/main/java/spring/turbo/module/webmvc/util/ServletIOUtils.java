/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * {@link jakarta.servlet.http.HttpServletRequest} 和 {@link HttpServletResponse} 相关IO工具
 *
 * @author 应卓
 * @since 3.3.1
 */
public final class ServletIOUtils {

    /**
     * 私有构造方法
     */
    private ServletIOUtils() {
        super();
    }

    public static void writeAsJson(HttpServletResponse response, String responseBody) throws IOException {
        writeAsJson(response, responseBody, UTF_8);
    }

    public static void writeAsJson(HttpServletResponse response, String responseBody, @Nullable Charset charset) throws IOException {
        charset = Objects.requireNonNullElse(charset, UTF_8);
        byte[] bytes = responseBody.getBytes(charset);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=" + charset.name());
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }

    public static void writeAsXml(HttpServletResponse response, String responseBody) throws IOException {
        writeAsXml(response, responseBody, UTF_8);
    }

    public static void writeAsXml(HttpServletResponse response, String responseBody, @Nullable Charset charset) throws IOException {
        charset = Objects.requireNonNullElse(charset, UTF_8);
        byte[] bytes = responseBody.getBytes(charset);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml;charset=" + charset.name());
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }

}
