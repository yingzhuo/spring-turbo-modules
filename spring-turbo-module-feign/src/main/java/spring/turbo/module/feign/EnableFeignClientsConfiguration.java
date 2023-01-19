/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
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
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import spring.turbo.bean.classpath.ClassDef;
import spring.turbo.bean.classpath.ClassPathScanner;
import spring.turbo.util.InstanceCache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 应卓
 * @since 2.0.9
 */
class EnableFeignClientsConfiguration implements
        ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware, ResourceLoaderAware {

    // 实际不为null
    private Environment environment;

    // 实际不为null
    private ResourceLoader resourceLoader;

    // 实际不为null
    private BeanFactory beanFactory; // TODO: 考虑如何将InstanceCache与BeanFactory整合，暂时没有用到

    /**
     * 默认构造方法
     */
    public EnableFeignClientsConfiguration() {
        super();
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
        final Set<String> basePackages = getBasePackages(importingClassMetadata);

        for (var definition : scanClasspath(basePackages)) {
            registerFeignClient(
                    registry,
                    beanNameGenerator,
                    definition
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void registerFeignClient(BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator, ClassDef classDef) {

        var metaAnnotation = classDef.getRequiredAnnotation(FeignClient.class);
        var clientType = classDef.getBeanClass();

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * 从 spring-turbo 2.0.9版本开始
         * 放弃使用 FactoryBean<T> 来创建对象实例
         * 此方式更直截了当，代码更容易理解。
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        var supplier = new FeignClientSupplier(
                InstanceCache.newInstance(),
                classDef.getBeanClass(),
                environment.resolveRequiredPlaceholders(metaAnnotation.url())
        );

        var clientBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clientType, supplier)
                .setLazyInit(classDef.isLazyInit())
                .setAbstract(false)
                .setRole(classDef.getRole())
                .getBeanDefinition();

        var beanName = metaAnnotation.value();
        if (beanName.isBlank()) {
            // 没有指定beanName，则生成一个
            beanName = beanNameGenerator.generateBeanName(clientBeanDefinition, registry);
        }

        registry.registerBeanDefinition(beanName, clientBeanDefinition);
    }

    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
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
        if (CollectionUtils.isEmpty(set)) {
            set.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return Collections.unmodifiableSet(set);
    }

    private List<ClassDef> scanClasspath(Set<String> basePackages) {
        return ClassPathScanner.builder()
                .environment(this.environment)
                .resourceLoader(this.resourceLoader)
                .includeFilter(new IncludeFilter())
                .build()
                .scan(basePackages);
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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @author 应卓
     * @since 1.0.1
     */
    private static final class IncludeFilter implements TypeFilter {

        @Override
        public boolean match(MetadataReader reader, MetadataReaderFactory readerFactory) {
            return reader.getAnnotationMetadata().hasAnnotation(FeignClient.class.getName()) &&
                    reader.getClassMetadata().isInterface();
        }
    }

}
