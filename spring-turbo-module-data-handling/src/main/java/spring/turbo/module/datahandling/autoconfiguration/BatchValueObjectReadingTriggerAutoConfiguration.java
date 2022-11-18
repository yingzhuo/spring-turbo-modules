/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.datahandling.excel.reader.BatchValueObjectReadingTrigger;
import spring.turbo.module.datahandling.excel.reader.BatchValueObjectReadingTriggerImpl;
import spring.turbo.module.datahandling.excel.reader.annotation.BatchProcessor;
import spring.turbo.module.datahandling.excel.visitor.BatchVisitor;

import java.util.List;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(BatchValueObjectReadingTrigger.class)
@ConditionalOnBean(value = BatchVisitor.class, annotation = BatchProcessor.class)
public class BatchValueObjectReadingTriggerAutoConfiguration {

    @Bean
    public BatchValueObjectReadingTrigger batchValueObjectReadingTrigger(List<BatchVisitor<?>> vs) {
        return new BatchValueObjectReadingTriggerImpl(vs);
    }

}
