/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.data;

import spring.turbo.core.ResourceLoaders;
import spring.turbo.util.EnumUtils;
import spring.turbo.util.StringFormatter;
import spring.turbo.util.StringPool;
import spring.turbo.util.StringUtils;
import spring.turbo.util.crypto.KeyStoreFormat;
import spring.turbo.util.crypto.KeyStoreHelper;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.security.Key;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class SymmetricKeyStoreEntryEditor extends PropertyEditorSupport implements PropertyEditor {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            setValue(doConvert(text));
        } catch (Throwable e) {
            String msg = StringFormatter.format("cannot convert to KeyStoreEntry: '{}'", text);
            throw new IllegalArgumentException(msg);
        }
    }

    private SymmetricKeyStoreEntry doConvert(String text) throws Throwable {
        var parts = text.split(StringPool.SEMICOLON);

        if (parts.length != 5) {
            throw new IllegalArgumentException();
        }

        var resource = ResourceLoaders.getDefault().getResource(StringUtils.removeAllWhitespaces(parts[0]));
        var format = toFormat(parts[1]);
        var storepass = StringUtils.removeAllWhitespaces(parts[2]);
        var alias = StringUtils.removeAllWhitespaces(parts[3]);
        var keypass = StringUtils.removeAllWhitespaces(parts[4]);

        var keyStore = KeyStoreHelper.loadKeyStore(resource.getInputStream(), format, storepass);
        var key = KeyStoreHelper.getKey(keyStore, alias, keypass);

        return new SymmetricKeyStoreEntryImpl(key, alias);
    }

    private KeyStoreFormat toFormat(String s) {
        s = StringUtils.removeAllWhitespaces(s);
        if ("p12".equalsIgnoreCase(s) || "pfx".equalsIgnoreCase(s)) {
            return KeyStoreFormat.PKCS12;
        }

        if ("jks".equalsIgnoreCase(s)) {
            return KeyStoreFormat.JKS;
        }

        return EnumUtils.getEnumIgnoreCase(KeyStoreFormat.class, s);
    }

    private record SymmetricKeyStoreEntryImpl(Key key, String alias) implements SymmetricKeyStoreEntry {
    }
}
