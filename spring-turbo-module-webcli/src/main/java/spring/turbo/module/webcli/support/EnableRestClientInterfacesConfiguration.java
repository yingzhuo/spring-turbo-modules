/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.support;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.turbo.bean.classpath.ClassDef;
import spring.turbo.bean.classpath.ClassPathScanner;
import spring.turbo.bean.classpath.PackageSetFactories;
import spring.turbo.util.StringUtils;
import spring.turbo.util.reflection.InstanceUtils;

import static spring.turbo.bean.classpath.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
@SuppressWarnings("unchecked")
class EnableRestClientInterfacesConfiguration implements ImportBeanDefinitionRegistrar {

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
        var annotationAttributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableRestClientInterfaces.class.getName())
        );

        if (annotationAttributes == null) {
            return;
        }

        var globalArgumentResolversSupplierClass =
                (Class<? extends ArgumentResolversSupplier>) annotationAttributes.getClass("globalArgumentResolversSupplier");

        var packageSet = PackageSetFactories.create(
                importingClassMetadata,
                EnableRestClientInterfaces.class,
                "basePackages",
                "basePackageClasses"
        );

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

        for (var classDef : classDefs) {
            registerOne(registry, ng, classDef, InstanceUtils.newInstanceElseThrow(globalArgumentResolversSupplierClass));
        }
    }

    private void registerOne(BeanDefinitionRegistry registry, BeanNameGenerator nameGen, ClassDef classDef, ArgumentResolversSupplier globalArgumentResolversSupplier) {

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

    public void addQualifiers(AbstractBeanDefinition beanDefinition, String... qualifiers) {
        for (var qualifier : qualifiers) {
            if (StringUtils.isNotBlank(qualifier)) {
                beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, qualifier));
            }
        }
    }

}
