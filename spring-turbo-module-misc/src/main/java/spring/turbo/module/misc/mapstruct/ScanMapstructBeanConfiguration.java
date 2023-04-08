/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.mapstruct;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.turbo.bean.classpath.ClassDef;
import spring.turbo.bean.classpath.ClassPathScanner;
import spring.turbo.bean.classpath.PackageSet;
import spring.turbo.util.Asserts;
import spring.turbo.util.ClassUtils;

import java.util.List;
import java.util.function.Supplier;

import static spring.turbo.bean.classpath.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 2.2.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class ScanMapstructBeanConfiguration implements ImportBeanDefinitionRegistrar {

    private final Environment environment;
    private final ClassLoader classLoader;
    private final ResourceLoader resourceLoader;

    public ScanMapstructBeanConfiguration(Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader) {
        this.environment = environment;
        this.classLoader = classLoader;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
        var packageSet = getBasePackages(importingClassMetadata);
        for (var clzDef : doScan(packageSet)) {
            this.registerMapper(clzDef, beanNameGenerator, registry);
        }
    }

    private PackageSet getBasePackages(AnnotationMetadata importingClassMetadata) {
        var packageSet = PackageSet.newInstance();

        var attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(ScanMapstructBean.class.getName())
        );

        if (attributes != null) {
            packageSet.acceptPackages(attributes.getStringArray("value"));
            packageSet.acceptBaseClasses(attributes.getClassArray("basePackageClasses"));
        }

        if (packageSet.isEmpty()) {
            packageSet.acceptPackages(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return packageSet;
    }

    private List<ClassDef> doScan(PackageSet basePackages) {
        var includeFilter = all(
                isInterface(),
                hasAnnotation(MapstructBean.class)
        );

        var excludeFilter = isPackageInfo();

        return ClassPathScanner.builder()
                .environment(this.environment)
                .resourceLoader(this.resourceLoader)
                .classLoader(this.classLoader)
                .includeFilter(includeFilter)
                .excludeFilter(excludeFilter)
                .build()
                .scan(basePackages);
    }

    private void registerMapper(ClassDef classDef, BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {

        var attributes = classDef.getAnnotationAttributes(MapstructBean.class);

        var supplier = new MapperSupplier(classDef.getBeanClass());
        var beanDef = BeanDefinitionBuilder.genericBeanDefinition(classDef.getBeanClass(), supplier)
                .setPrimary(attributes.getBoolean("primary"))
                .setLazyInit(classDef.isLazyInit())
                .setAbstract(false)
                .setRole(classDef.getRole())
                .getBeanDefinition();

        var beanName = attributes.getString("beanName");
        if (beanName.isBlank()) {
            beanName = beanNameGenerator.generateBeanName(beanDef, registry);
        }

        addQualifiers(beanDef, attributes.getStringArray("qualifiers"));

        registry.registerBeanDefinition(beanName, beanDef);
    }

    public void addQualifiers(AbstractBeanDefinition beanDefinition, String... qualifiers) {
        for (var q : qualifiers) {
            beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, q));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private record MapperSupplier(Class<?> mapperClass) implements Supplier {

        private MapperSupplier {
            Asserts.notNull(mapperClass);
        }

        @Override
        public Object get() {
            return Mappers.getMapper(this.mapperClass);
        }
    }

}
