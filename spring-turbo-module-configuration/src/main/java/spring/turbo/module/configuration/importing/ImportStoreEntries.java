package spring.turbo.module.configuration.importing;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ImportStoreEntryConfig.class)
public @interface ImportStoreEntries {

    public ImportStoreEntry[] value() default {};

}
