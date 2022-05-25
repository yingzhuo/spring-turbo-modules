/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
@NonNullApi
@NonNullFields
package spring.turbo.module.excel.reader;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import spring.turbo.module.excel.reader.annotation.BatchProcessor;
import spring.turbo.module.excel.visitor.BatchVisitor;

import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
@AutoConfiguration
class SpringBootAutoConfiguration {

    @Bean
    @ConditionalOnBean(value = BatchVisitor.class, annotation = BatchProcessor.class)
    BatchValueObjectReadingTrigger batchValueObjectReadingTrigger(List<BatchVisitor<?>> vs) {
        return new BatchValueObjectReadingTriggerImpl(vs);
    }

}
