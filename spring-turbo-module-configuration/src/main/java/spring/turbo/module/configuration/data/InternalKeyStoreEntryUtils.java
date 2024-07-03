/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.data;

import spring.turbo.module.configuration.util.ParameterExtractor;
import spring.turbo.util.EnumUtils;
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

        if (text == null) {
            throw new IllegalArgumentException();
        }

        text = text.trim();
        if ("p12".equalsIgnoreCase(text) || "pfx".equalsIgnoreCase(text) || "pkcs#12".equalsIgnoreCase(text) || "pkcs12".equalsIgnoreCase(text)) {
            return KeyStoreFormat.PKCS12;
        }

        if ("jks".equalsIgnoreCase(text)) {
            return KeyStoreFormat.JKS;
        }

        var format = EnumUtils.getEnumIgnoreCase(KeyStoreFormat.class, text);
        if (format == null) {
            throw new IllegalArgumentException();
        }
        return format;
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
