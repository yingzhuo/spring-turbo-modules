package spring.turbo.module.configuration.util;

import org.springframework.lang.Nullable;
import spring.turbo.util.StringFormatter;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.MULTILINE;
import static spring.turbo.util.StringPool.SEMICOLON;

/**
 * 内部工具，用于提取字符串中的参数
 *
 * @author 应卓
 * @since 3.3.1
 */
public final class ParameterExtractor {

    /**
     * 私有构造方法
     */
    private ParameterExtractor() {
    }

    @Nullable
    public static String parameterValue(String text, String parameterName) {
        return parameterValue(text, parameterName, SEMICOLON);
    }

    @Nullable
    public static String parameterValue(String text, String parameterName, @Nullable String endString) {
        endString = Objects.requireNonNullElse(endString, SEMICOLON);
        var regx = StringFormatter.format("{}=(.*?){}", parameterName, endString);
        var pattern = Pattern.compile(regx, DOTALL | MULTILINE);
        var matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
