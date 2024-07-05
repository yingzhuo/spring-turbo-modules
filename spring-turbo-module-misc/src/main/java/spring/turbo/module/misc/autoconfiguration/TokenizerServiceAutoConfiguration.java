package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.misc.tokenizer.TokenizerService;
import spring.turbo.module.misc.tokenizer.TokenizerServiceFactoryBean;

/**
 * @author 应卓
 * @since 3.1.1
 */
@AutoConfiguration
@ConditionalOnMissingBean(TokenizerService.class)
@ConditionalOnClass(name = "cn.hutool.extra.tokenizer.TokenizerEngine")
public class TokenizerServiceAutoConfiguration {

    @Bean
    public TokenizerServiceFactoryBean tokenizerService() {
        return new TokenizerServiceFactoryBean();
    }

}
