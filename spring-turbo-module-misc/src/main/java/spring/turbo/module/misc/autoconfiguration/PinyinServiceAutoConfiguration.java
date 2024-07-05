package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.misc.pinyin.PinyinService;
import spring.turbo.module.misc.pinyin.PinyinServiceImpl;

/**
 * @author 应卓
 * @since 3.1.0
 */
@AutoConfiguration
@ConditionalOnClass(name = "net.sourceforge.pinyin4j.PinyinHelper")
public class PinyinServiceAutoConfiguration {

    @Bean
    public PinyinService pinyinService() {
        return new PinyinServiceImpl();
    }

}
