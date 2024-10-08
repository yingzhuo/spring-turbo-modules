package spring.turbo.module.webcli.annotation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.turbo.bean.classpath.ClassDefinition;
import spring.turbo.bean.classpath.ClassPathScanner;
import spring.turbo.bean.classpath.PackageSet;
import spring.turbo.util.ClassUtils;
import spring.turbo.util.StringUtils;
import spring.turbo.util.reflection.InstanceUtils;

import static spring.turbo.bean.classpath.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
@SuppressWarnings("unchecked")
class EnableRestClientInterfacesConfiguration implements ImportBeanDefinitionRegistrar {

    private static final Class<EnableRestClientInterfaces> IMPORTING_ANNOTATION_CLASS = EnableRestClientInterfaces.class;

    private final ClassLoader classLoader;
    private final Environment environment;
    private final ResourceLoader resourceLoader;

    public EnableRestClientInterfacesConfiguration(ClassLoader classLoader, Environment environment, ResourceLoader resourceLoader) {
        this.classLoader = classLoader;
        this.environment = environment;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator ng) {
        var importingAnnotationAttributes =
                AnnotationAttributes.fromMap(
                        importingClassMetadata.getAnnotationAttributes(IMPORTING_ANNOTATION_CLASS.getName(), false)
                );

        if (importingAnnotationAttributes == null) {
            // 这种情况实际不会发生
            return;
        }

        var packageSet = PackageSet.newInstance()
                .acceptPackages(importingAnnotationAttributes.getStringArray("basePackages"))
                .acceptBaseClasses(importingAnnotationAttributes.getClassArray("basePackageClasses"));

        if (packageSet.isEmpty()) {
            packageSet.acceptPackages(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        var classDefs = ClassPathScanner.builder()
                .classLoader(classLoader)
                .resourceLoader(resourceLoader)
                .environment(environment)
                .includeFilter(all(
                        isInterface(),
                        isNotPackageInfo(),
                        hasAnnotation(RestClientInterface.class)
                ))
                .build()
                .scan(packageSet);

        if (!classDefs.isEmpty()) {
            var globalArgumentResolversSupplierClass =
                    (Class<? extends ArgumentResolversSupplier>) importingAnnotationAttributes.getClass("globalArgumentResolversSupplier");

            var globalArgumentResolversSupplier = InstanceUtils.newInstanceElseThrow(globalArgumentResolversSupplierClass);

            for (var classDef : classDefs) {
                registerOne(registry, ng, classDef, globalArgumentResolversSupplier);
            }
        }
    }

    private void registerOne(BeanDefinitionRegistry registry, BeanNameGenerator nameGen, ClassDefinition classDef, ArgumentResolversSupplier globalArgumentResolversSupplier) {

        var metaAnnotation = classDef.getRequiredAnnotation(RestClientInterface.class);
        var clientSupplier = InstanceUtils.newInstanceElseThrow(metaAnnotation.clientSupplier());
        var argumentResolversSupplier = InstanceUtils.newInstanceElseThrow(metaAnnotation.argumentResolversSupplier());

        var interfaceGen = new RestClientInterfaceGen(
                classDef,
                environment,
                clientSupplier,
                globalArgumentResolversSupplier,
                argumentResolversSupplier
        );

        var beanType = classDef.getBeanClass();
        var clientBeanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(beanType, interfaceGen)
                        .setPrimary(metaAnnotation.primary())
                        .setLazyInit(classDef.isLazyInit())
                        .setAbstract(false)
                        .setRole(classDef.getRole())
                        .getBeanDefinition();

        addQualifiers(clientBeanDefinition, metaAnnotation.qualifiers());

        var beanName = metaAnnotation.value();

        if (beanName.isBlank()) {
            beanName = nameGen.generateBeanName(clientBeanDefinition, registry);
        }

        registry.registerBeanDefinition(beanName, clientBeanDefinition);
    }

    private void addQualifiers(AbstractBeanDefinition beanDefinition, String... qualifiers) {
        for (var qualifier : qualifiers) {
            if (StringUtils.isNotBlank(qualifier)) {
                beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, qualifier));
            }
        }
    }

}
