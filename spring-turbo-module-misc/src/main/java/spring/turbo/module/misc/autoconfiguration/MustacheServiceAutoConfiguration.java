package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.misc.mustache.MustacheService;
import spring.turbo.module.misc.mustache.MustacheServiceImpl;

/**
 * @author 应卓
 * @since 3.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(MustacheService.class)
@ConditionalOnClass(name = "com.github.mustachejava.MustacheFactory")
public class MustacheServiceAutoConfiguration {

    @Bean
    public MustacheService mustacheService() {
        return new MustacheServiceImpl();
    }

}
