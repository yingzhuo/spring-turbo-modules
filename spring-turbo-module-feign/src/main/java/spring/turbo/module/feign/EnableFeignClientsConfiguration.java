/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.feign;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import spring.turbo.bean.ClassPathScanner;
import spring.turbo.bean.ScannedResult;
import spring.turbo.core.SpringContext;
import spring.turbo.core.SpringContextAware;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static spring.turbo.bean.TypeFilterFactories.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
class EnableFeignClientsConfiguration implements SpringContextAware, ImportBeanDefinitionRegistrar {

    private SpringContext springContext;
    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setSpringContext(SpringContext springContext) {
        this.springContext = springContext;
        this.environment = springContext.getEnvironment();
        this.resourceLoader = springContext.getResourceLoader();
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

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
