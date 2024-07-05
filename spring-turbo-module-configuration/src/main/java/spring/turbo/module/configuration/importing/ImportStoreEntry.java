package spring.turbo.module.configuration.importing;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import spring.turbo.util.crypto.KeyStoreFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 应卓
 * @since 3.3.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ImportStoreEntryConfig.class)
public @interface ImportStoreEntry {

    @AliasFor("beanName")
    public String value() default "";

    @AliasFor("value")
    public String beanName() default "";

    public KeyType keyType() default KeyType.ASYMMETRIC;

    public String location();

    public KeyStoreFormat keyStoreFormat() default KeyStoreFormat.PKCS12;

    public String storepass();

    public String alias();

    public String keypass();

    // -----------------------------------------------------------------------------------------------------------------

    public static enum KeyType {

        /**
         * 非对称
         */
        ASYMMETRIC,

        /**
         * 对称
         */
        SYMMETRIC
    }

}
