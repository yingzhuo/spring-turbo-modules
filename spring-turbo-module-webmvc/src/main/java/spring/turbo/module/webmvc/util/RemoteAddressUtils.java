package spring.turbo.module.webmvc.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Objects;

/**
 * 远程IP地址工具
 *
 * @author 应卓
 * @since 1.0.0
 */
public final class RemoteAddressUtils {

    /**
     * 私有构造方法
     */
    private RemoteAddressUtils() {
    }

    /**
     * 获取远程IP地址
     *
     * @param request HTTP请求
     * @return ip地址
     */
    @Nullable
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取远程IP地址
     *
     * @param request HTTP请求
     * @return ip地址
     */
    public static String getRequiredIpAddress(HttpServletRequest request) {
        var ip = getIpAddress(request);
        return Objects.requireNonNull(ip);
    }

    /**
     * 获取远程IP地址
     *
     * @param request HTTP请求
     * @return ip地址
     */
    @Nullable
    public static String getIpAddress(NativeWebRequest request) {
        final HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
        return getIpAddress(httpServletRequest);
    }

    /**
     * 获取远程IP地址
     *
     * @param request HTTP请求
     * @return ip地址
     */
    public static String getRequiredIpAddress(NativeWebRequest request) {
        var ip = getIpAddress(request);
        return Objects.requireNonNull(ip);
    }

}
