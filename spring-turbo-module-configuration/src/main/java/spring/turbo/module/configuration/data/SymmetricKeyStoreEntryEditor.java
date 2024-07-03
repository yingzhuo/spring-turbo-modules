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
import spring.turbo.util.StringFormatter;
import spring.turbo.util.crypto.KeyStoreHelper;

import java.beans.PropertyEditorSupport;
import java.security.Key;

import static spring.turbo.module.configuration.data.InternalKeyStoreEntryUtils.*;

/**
 * 支持多行或单行字符串转化成 {@link SymmetricKeyStoreEntry}。
 *
 * <pre>
 * location=classpath:/secret/key-store-123456.p12; format=PKCS#12; storepass=123456; alias=rsa; keypass=123456;
 * </pre>
 *
 * @author 应卓
 * @since 3.3.1
 */
@SuppressWarnings("DuplicatedCode")
public class SymmetricKeyStoreEntryEditor extends PropertyEditorSupport {

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

        var location = toResourceLocation(text);
        var format = toFormat(text);
        var storepass = toStorepass(text);
        var alias = toAlias(text);
        var keypass = toKeypass(text);

        var resource = ResourceLoaders.getDefault().getResource(location);
        var keyStore = KeyStoreHelper.loadKeyStore(resource.getInputStream(), format, storepass);
        var key = KeyStoreHelper.getKey(keyStore, alias, keypass);

        return new SymmetricKeyStoreEntryImpl(key, alias);
    }

    private record SymmetricKeyStoreEntryImpl(Key key, String alias) implements SymmetricKeyStoreEntry {
    }

}
