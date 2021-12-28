/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

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
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import spring.turbo.bean.ClassDefinition;
import spring.turbo.bean.ClassPathScanner;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.0.0
 */
class EnableFeignClientsConfiguration implements
        ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {

        final Set<String> basePackages = getBasePackages(importingClassMetadata);

        if (CollectionUtils.isEmpty(basePackages)) {
            return;
        }

        for (ClassDefinition definition : scanClasspath(basePackages)) {
            registerFeignClient(
                    registry,
                    beanNameGenerator,
                    definition.getBeanClass()
            );
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

    private void registerFeignClient(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator, Class<?> feignClientType) {
        final FeignClient primaryAnnotation = AnnotationUtils.findAnnotation(feignClientType, FeignClient.class);
        if (primaryAnnotation == null) {
            return;
        }

        final AbstractBeanDefinition factoryBeanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(FeignClientFactoryBean.class)
                        .addPropertyValue("clientType", feignClientType)
                        .addPropertyValue("url", primaryAnnotation.url())
                        .getBeanDefinition();

        factoryBeanDefinition.setAttribute(FeignClientFactoryBean.OBJECT_TYPE_ATTRIBUTE, FeignClientFactoryBean.class.getName());
        factoryBeanDefinition.setResourceDescription("spring-turbo-module-feign :)"); // 彩蛋

        String beanName = primaryAnnotation.value();
        if (StringUtils.isBlank(beanName)) {
            // 没有指定beanName，则生成一个
            beanName = beanNameGenerator.generateBeanName(factoryBeanDefinition, registry);
        }
        registry.registerBeanDefinition(beanName, factoryBeanDefinition);
    }

    @NonNull
    private Set<String> getBasePackages(@NonNull AnnotationMetadata importingClassMetadata) {
        final Set<String> set = new HashSet<>();
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableFeignClients.class.getName())
        );
        if (!CollectionUtils.isEmpty(attributes)) {
            Collections.addAll(set, attributes.getStringArray("value"));
            for (Class<?> clz : attributes.getClassArray("basePackageClasses")) {
                set.add(clz.getPackage().getName());
            }
        }
        return Collections.unmodifiableSet(set);
    }

    private List<ClassDefinition> scanClasspath(@NonNull Set<String> basePackages) {
        return ClassPathScanner.builder()
                .environment(this.environment)
                .resourceLoader(this.resourceLoader)
                .includeFilter(new IncludeTypeFilter())
                .build()
                .scan(basePackages);
    }

    /**
     * @author 应卓
     * @since 1.0.1
     */
    private static class IncludeTypeFilter implements TypeFilter {
        @Override
        public boolean match(MetadataReader reader, MetadataReaderFactory readerFactory) {
            final boolean condition1 = reader.getAnnotationMetadata().hasAnnotation(FeignClient.class.getName());
            final boolean condition2 = reader.getClassMetadata().isInterface();
            return condition1 && condition2;
        }
    }

}
