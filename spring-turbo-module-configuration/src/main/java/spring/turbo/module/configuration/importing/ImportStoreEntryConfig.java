package spring.turbo.module.configuration.importing;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.turbo.module.configuration.data.AsymmetricKeyStoreEntry;
import spring.turbo.module.configuration.data.KeyStoreEntry;
import spring.turbo.module.configuration.data.SymmetricKeyStoreEntry;
import spring.turbo.util.crypto.KeyStoreFormat;
import spring.turbo.util.crypto.KeyStoreHelper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 3.3.1
 */
class ImportStoreEntryConfig implements ImportBeanDefinitionRegistrar {

    private static final String IMPORTING_ANNOTATION_TYPE = ImportStoreEntry.class.getName();
    private static final String IMPORTING_ANNOTATION_REPEAT_TYPE = ImportStoreEntries.class.getName();

    private final Environment environment;
    private final ResourceLoader resourceLoader;

    public ImportStoreEntryConfig(Environment environment, ResourceLoader resourceLoader) {
        this.environment = environment;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator) {

        final var allAnnotations = new ArrayList<AnnotationAttributes>();

        // 单个
        // -------------------------------------------------------------------------------------------------------------
        var singleAnnotationAttributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(IMPORTING_ANNOTATION_TYPE)
                );

        if (singleAnnotationAttributes != null) {
            allAnnotations.add(singleAnnotationAttributes);
        }
        // -------------------------------------------------------------------------------------------------------------


        // 多个
        // -------------------------------------------------------------------------------------------------------------
        var repeatedAnnotationAttributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(IMPORTING_ANNOTATION_REPEAT_TYPE)
                );

        if (repeatedAnnotationAttributes != null) {
            var repeatedAnnotations = repeatedAnnotationAttributes.getAnnotationArray("value");
            Collections.addAll(allAnnotations, repeatedAnnotations);
        }
        // -------------------------------------------------------------------------------------------------------------


        for (var annotation : allAnnotations) {
            doRegister(annotation, registry, nameGenerator);
        }
    }

    private void doRegister(AnnotationAttributes annotation, BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator) {

        var beanName = annotation.getString("beanName");
        beanName = environment.resolvePlaceholders(beanName);

        ImportStoreEntry.KeyType keyType = annotation.getEnum("keyType");

        var location = annotation.getString("location");
        location = environment.resolvePlaceholders(location);

        KeyStoreFormat keyStoreFormat = annotation.getEnum("keyStoreFormat");

        var storepass = annotation.getString("storepass");
        storepass = environment.resolvePlaceholders(storepass);

        var alias = annotation.getString("alias");
        alias = environment.resolvePlaceholders(alias);

        var keypass = annotation.getString("keypass");
        keypass = environment.resolvePlaceholders(keypass);

        var definition =
                BeanDefinitionBuilder.genericBeanDefinition(
                                KeyStoreEntry.class,
                                new KeyStoreEntryProvider(
                                        resourceLoader,
                                        keyType,
                                        location,
                                        keyStoreFormat,
                                        storepass,
                                        alias,
                                        keypass
                                )
                        )
                        .setPrimary(true)
                        .setLazyInit(false)
                        .setAbstract(false)
                        .setScope(BeanDefinition.SCOPE_SINGLETON)
                        .getBeanDefinition();

        if (beanName.isBlank()) {
            beanName = nameGenerator.generateBeanName(definition, registry);
        }

        registry.registerBeanDefinition(beanName, definition);
    }

    private record KeyStoreEntryProvider(ResourceLoader resourceLoader,
                                         ImportStoreEntry.KeyType keyType,
                                         String location,
                                         KeyStoreFormat keyStoreFormat,
                                         String storepass,
                                         String alias, String keypass) implements Supplier<KeyStoreEntry> {

        @Override
        public KeyStoreEntry get() {
            try {
                return doGen();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private KeyStoreEntry doGen() throws IOException {
            final var keyStore = KeyStoreHelper.loadKeyStore(
                    resourceLoader.getResource(location).getInputStream(),
                    keyStoreFormat,
                    storepass
            );

            if (keyType == ImportStoreEntry.KeyType.ASYMMETRIC) {
                final var keyPair = KeyStoreHelper.getKeyPair(keyStore, alias, keypass);
                final var cert = KeyStoreHelper.getCertificate(keyStore, alias);

                return new AsymmetricKeyStoreEntry() {
                    @Override
                    public KeyPair keyPair() {
                        return keyPair;
                    }

                    @Override
                    public Certificate certificate() {
                        return cert;
                    }

                    @Override
                    public String alias() {
                        return alias;
                    }
                };
            } else {
                final var key = KeyStoreHelper.getKey(keyStore, alias, keypass);
                return new SymmetricKeyStoreEntry() {
                    @Override
                    public Key key() {
                        return key;
                    }

                    @Override
                    public String alias() {
                        return alias;
                    }
                };
            }
        }
    }

}
