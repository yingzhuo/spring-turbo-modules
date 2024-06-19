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
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.turbo.bean.classpath.ClassDef;
import spring.turbo.bean.classpath.ClassPathScanner;
import spring.turbo.bean.classpath.PackageSet;
import spring.turbo.bean.classpath.PackageSetFactories;
import spring.turbo.util.InstanceUtils;
import spring.turbo.util.StringUtils;

import java.util.List;

import static spring.turbo.bean.classpath.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 3.3.1
 */
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
        var packageSet = PackageSetFactories.create(
                importingClassMetadata,
                EnableRestClientInterfaces.class,
                "basePackages",
                "basePackageClasses"
        );

        var classDefs = doScan(packageSet);

        for (var classDef : classDefs) {
            registerOne(registry, ng, classDef);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerOne(BeanDefinitionRegistry registry, BeanNameGenerator nameGen, ClassDef classDef) {

        var metaAnnotation = classDef.getRequiredAnnotation(RestClientInterface.class);
        var clientSupplier = InstanceUtils.newInstanceElseThrow(metaAnnotation.clientSupplier());
        var argumentResolversSupplier = InstanceUtils.newInstanceElseThrow(metaAnnotation.argumentResolversSupplier());

        var interfaceFactory = new RestClientInterfaceFactory(classDef, clientSupplier, argumentResolversSupplier);

        var beanType = classDef.getBeanClass();
        var clientBeanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(beanType, interfaceFactory)
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

    private List<ClassDef> doScan(PackageSet basePackages) {
        return ClassPathScanner.builder()
                .classLoader(classLoader)
                .resourceLoader(resourceLoader)
                .environment(environment)
                .includeFilter(all(
                        hasAnnotation(RestClientInterface.class), isInterface()
                ))
                .excludeFilter(isPackageInfo())
                .build()
                .scan(basePackages);
    }

    public void addQualifiers(AbstractBeanDefinition beanDefinition, String... qualifiers) {
        for (var qualifier : qualifiers) {
            if (StringUtils.isNotBlank(qualifier)) {
                beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, qualifier));
            }
        }
    }

}