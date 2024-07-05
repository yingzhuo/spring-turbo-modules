package spring.turbo.module.configuration.data;

import spring.turbo.module.configuration.util.ParameterExtractor;
import spring.turbo.util.crypto.KeyStoreFormat;

import java.util.Optional;

/**
 * 内部工具
 *
 * @author 应卓
 * @since 3.3.1
 */
abstract class InternalKeyStoreEntryUtils {

    public static KeyStoreFormat toFormat(String text) {
        text = ParameterExtractor.parameterValue(text, "format");
        return Optional.ofNullable(text)
                .map(String::trim)
                .map(KeyStoreFormat::fromValue)
                .orElseGet(KeyStoreFormat::getDefault);
    }

    public static String toResourceLocation(String text) {
        var location = ParameterExtractor.parameterValue(text, "location");
        return Optional.ofNullable(location)
                .map(String::trim)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static String toStorepass(String text) {
        var storepass = ParameterExtractor.parameterValue(text, "storepass");
        return Optional.ofNullable(storepass)
                .map(String::trim)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static String toAlias(String text) {
        var alias = ParameterExtractor.parameterValue(text, "alias");
        return Optional.ofNullable(alias)
                .map(String::trim)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static String toKeypass(String text) {
        var keypass = ParameterExtractor.parameterValue(text, "keypass");
        return Optional.ofNullable(keypass)
                .map(String::trim)
                .orElseThrow(IllegalArgumentException::new);
    }

}
