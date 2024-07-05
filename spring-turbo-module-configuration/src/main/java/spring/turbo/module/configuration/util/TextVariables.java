package spring.turbo.module.configuration.util;

import org.springframework.lang.Nullable;
import spring.turbo.util.StringMatcher;
import spring.turbo.util.StringTokenizer;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.MULTILINE;
import static spring.turbo.util.StringPool.SEMICOLON;

/**
 * 工具类，从文本中解析出若干参数
 *
 * @author 应卓
 * @since 3.3.1
 */
public class TextVariables implements Serializable {

    private final Map<String, String> delegatingMap = new TreeMap<>(Comparator.naturalOrder());

    public TextVariables(@Nullable String text) {
        reset(text);
    }

    public TextVariables(@Nullable String text, @Nullable String delim) {
        reset(text, delim);
    }

    public TextVariables(@Nullable String text, @Nullable StringMatcher delim) {
        reset(text, delim);
    }

    public Set<String> getNames() {
        return this.delegatingMap.keySet();
    }

    @Nullable
    public String getValue(String name) {
        return this.delegatingMap.get(name);
    }

    public String getValue(String name, String defaultValue) {
        var value = getValue(name);
        if (value != null && !value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    public int size() {
        return this.delegatingMap.size();
    }

    public boolean isEmpty() {
        return this.delegatingMap.isEmpty();
    }

    public boolean isNotEmpty() {
        return !this.delegatingMap.isEmpty();
    }

    public void clear() {
        this.delegatingMap.clear();
    }

    public void reset(@Nullable String text) {
        reset(text, (StringMatcher) null);
    }

    public void reset(@Nullable String text, @Nullable String delim) {
        reset(text, StringMatcher.stringMatcher(Objects.requireNonNullElse(delim, SEMICOLON)));
    }

    public void reset(@Nullable String text, @Nullable StringMatcher delim) {
        this.delegatingMap.clear();

        if (text == null || !text.isBlank()) {
            delim = Objects.requireNonNullElse(delim, StringMatcher.stringMatcher(SEMICOLON));

            var stringTokenizer = new StringTokenizer(text, delim);

            while (stringTokenizer.hasNext()) {
                var token = stringTokenizer.next();

                if (!token.isBlank()) {
                    var regex = "(.*?)=(.*)";
                    var pattern = Pattern.compile(regex, DOTALL | MULTILINE);
                    var matcher = pattern.matcher(token);

                    if (matcher.matches()) {
                        var variableName = matcher.group(1).trim();
                        var variableValue = matcher.group(2).trim();
                        delegatingMap.put(variableName, variableValue);
                    }
                }
            }
        }
    }

}
