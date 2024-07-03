/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.util;

import org.springframework.lang.Nullable;
import spring.turbo.util.StringFormatter;
import spring.turbo.util.StringPool;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author 应卓
 * @since 3.3.1
 */
public final class ParameterExtractor {

    /**
     * 私有构造方法
     */
    private ParameterExtractor() {
        super();
    }

    @Nullable
    public static String parameterValue(String text, String parameterName) {
        return parameterValue(text, parameterName, StringPool.SEMICOLON);
    }

    @Nullable
    public static String parameterValue(String text, String parameterName, @Nullable String endString) {
        endString = Objects.requireNonNullElse(endString, StringPool.SEMICOLON);
        var regx = StringFormatter.format("{}=(.*?){}", parameterName, endString);
        var pattern = Pattern.compile(regx, Pattern.DOTALL | Pattern.MULTILINE);
        var matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
