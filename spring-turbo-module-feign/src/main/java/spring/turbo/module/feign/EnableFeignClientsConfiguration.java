/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import spring.turbo.bean.classpath.ClassDef;
import spring.turbo.bean.classpath.ClassPathScanner;
import spring.turbo.bean.classpath.PackageSet;
import spring.turbo.util.ClassUtils;
import spring.turbo.util.InstanceCache;

import java.util.List;

import static spring.turbo.bean.classpath.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 2.0.9
 */
class EnableFeignClientsConfiguration implements
        ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware, ResourceLoaderAware, BeanClassLoaderAware {

    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private Environment environment;
    private ResourceLoader resourceLoader;

    /**
     * 默认构造方法
     */
    public EnableFeignClientsConfiguration() {
        super();
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator) {
        var basePackages = getBasePackages(importingClassMetadata);

        for (var definition : doScan(basePackages)) {
            registerFeignClient(
                    registry,
                    nameGenerator,
                    definition
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void registerFeignClient(BeanDefinitionRegistry registry, BeanNameGenerator nameGenerator, ClassDef classDef) {

        var metaAnnotation = classDef.getRequiredAnnotation(FeignClient.class);
        var clientType = classDef.getBeanClass();

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * 从 spring-turbo 2.0.9版本开始
         * 放弃使用 FactoryBean<T> 来创建对象实例
         * 此方式更直截了当，代码更容易理解。
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        var supplier = new FeignClientSupplier(
                InstanceCache.newInstance(beanFactory),
                classDef.getBeanClass(),
                environment.resolveRequiredPlaceholders(metaAnnotation.url())
        );

        var clientBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clientType, supplier)
                .setPrimary(metaAnnotation.primary())
                .setLazyInit(classDef.isLazyInit())
                .setAbstract(false)
                .setRole(classDef.getRole())
                .getBeanDefinition();

        addQualifiers(clientBeanDefinition, metaAnnotation.qualifiers());

        var beanName = metaAnnotation.value();
        if (beanName.isBlank()) {
            beanName = nameGenerator.generateBeanName(clientBeanDefinition, registry);
        }

        registry.registerBeanDefinition(beanName, clientBeanDefinition);
    }

    private PackageSet getBasePackages(AnnotationMetadata importingClassMetadata) {
        var packageSet = PackageSet.newInstance();

        var attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableFeignClients.class.getName())
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
        var typeFilter = all(
                isInterface(),
                hasAnnotation(FeignClient.class)
        );

        return ClassPathScanner.builder()
                .environment(this.environment)
                .resourceLoader(this.resourceLoader)
                .classLoader(this.classLoader)
                .includeFilter(typeFilter)
                .build()
                .scan(basePackages);
    }

    public void addQualifiers(AbstractBeanDefinition beanDefinition, String... qualifiers) {
        for (var q : qualifiers) {
            beanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, q));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
