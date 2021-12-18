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
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import spring.turbo.bean.ClassPathScanner;
import spring.turbo.bean.ScannedResult;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static spring.turbo.bean.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
class EnableFeignClientsConfiguration implements
        ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        final Set<String> basePackages = getBasePackages(importingClassMetadata);

        for (ScannedResult scannedResult : scanClassPath(basePackages)) {
            registerFeignClient(registry, importBeanNameGenerator, scannedResult);
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

    private void registerFeignClient(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator, ScannedResult scannedResult) {
        final String feignClientTypeString = scannedResult.getClassName();
        final Class<?> feignClientType = loadClass(feignClientTypeString);

        FeignClient primaryAnnotation = AnnotationUtils.findAnnotation(feignClientType, FeignClient.class);
        if (primaryAnnotation == null) {
            return;
        }

        final AbstractBeanDefinition beanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(FeignClientFactoryBean.class)
                        .addPropertyValue("clientType", feignClientType)
                        .addPropertyValue("url", primaryAnnotation.url())
                        .getBeanDefinition();

        beanDefinition.setAttribute(FeignClientFactoryBean.OBJECT_TYPE_ATTRIBUTE, FeignClientFactoryBean.class.getName());

        String beanName = primaryAnnotation.value();
        if (StringUtils.isBlank(beanName)) {
            beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        }
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private Class<?> loadClass(String classString) {
        try {
            return ClassUtils.forName(classString, ClassUtils.getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @NonNull
    private Set<String> getBasePackages(@NonNull AnnotationMetadata importingClassMetadata) {
        Set<String> set = new HashSet<>();
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

    private Set<ScannedResult> scanClassPath(@NonNull Set<String> basePackages) {
        if (basePackages.isEmpty()) {
            return Collections.emptySet();
        }

        return ClassPathScanner.builder()
                .environment(this.environment)
                .resourceLoader(this.resourceLoader)
                .includeFilter(
                        and(
                                annotation(FeignClient.class),
                                isInterface()
                        )
                )
                .build()
                .scan(basePackages);
    }

}
