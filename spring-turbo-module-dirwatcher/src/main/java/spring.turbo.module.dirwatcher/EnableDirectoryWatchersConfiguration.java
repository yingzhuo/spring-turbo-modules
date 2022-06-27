/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dirwatcher;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import spring.turbo.bean.AbstractFactoryBean;
import spring.turbo.bean.ClassDefinition;
import spring.turbo.bean.ClassPathScanner;
import spring.turbo.util.Asserts;
import spring.turbo.util.InstanceUtils;
import spring.turbo.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.1.1
 */
class EnableDirectoryWatchersConfiguration implements
        ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    @Nullable
    private Environment environment;

    @Nullable
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {

        final Set<String> basePackages = getBasePackages(importingClassMetadata);

        if (CollectionUtils.isEmpty(basePackages)) {
            return;
        }

        for (ClassDefinition definition : scanClasspath(basePackages)) {
            registerWatcher(
                    registry,
                    beanNameGenerator,
                    definition
            );
        }
    }

    private void registerWatcher(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator, ClassDefinition definition) {
        // double check
        final Watch primaryAnnotation = definition.findAnnotation(Watch.class);
        if (primaryAnnotation == null) {
            return;
        }

        final AbstractBeanDefinition factoryBeanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(WatcherFactoryBean.class)
                        .addPropertyValue("listener", InstanceUtils.newInstanceOrThrow(definition.getBeanClass()))
                        .addPropertyValue("spel", primaryAnnotation.spel())
                        .getBeanDefinition();

        factoryBeanDefinition.setAttribute(AbstractFactoryBean.OBJECT_TYPE_ATTRIBUTE, WatcherFactoryBean.class.getName());
        factoryBeanDefinition.setPrimary(definition.isPrimary());
        factoryBeanDefinition.setAbstract(definition.isAbstractDefinition());
        factoryBeanDefinition.setRole(definition.getRole());
        factoryBeanDefinition.setLazyInit(false);
        factoryBeanDefinition.setResourceDescription("spring-turbo-module-dirwatcher"); // 彩蛋

        String beanName = primaryAnnotation.beanName();
        if (StringUtils.isBlank(beanName)) {
            // 没有指定beanName，则生成一个
            beanName = beanNameGenerator.generateBeanName(factoryBeanDefinition, registry);
        }
        registry.registerBeanDefinition(beanName, factoryBeanDefinition);
    }

    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        final Set<String> set = new HashSet<>();
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableDirectoryWatchers.class.getName())
        );
        if (!CollectionUtils.isEmpty(attributes)) {
            Collections.addAll(set, attributes.getStringArray("value"));
            for (Class<?> clz : attributes.getClassArray("basePackageClasses")) {
                set.add(clz.getPackage().getName());
            }
        }
        if (CollectionUtils.isEmpty(set)) {
            set.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return Collections.unmodifiableSet(set);
    }

    private List<ClassDefinition> scanClasspath(Set<String> basePackages) {
        Asserts.notNull(resourceLoader);
        Asserts.notNull(environment);
        return ClassPathScanner.builder()
                .environment(this.environment)
                .resourceLoader(this.resourceLoader)
                .includeFilter(new IncludeTypeFilter())
                .build()
                .scan(basePackages);
    }

    private static class IncludeTypeFilter implements TypeFilter {
        @Override
        public boolean match(MetadataReader reader, MetadataReaderFactory readerFactory) {
            final boolean condition1 = reader.getAnnotationMetadata().hasAnnotation(Watch.class.getName());
            final boolean condition2 = reader.getClassMetadata().isConcrete();
            return condition1 && condition2;
        }
    }

}
