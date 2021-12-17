/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.excel.reader.annotation.BatchedProcessor;
import spring.turbo.module.excel.visitor.BatchedVisitor;
import spring.turbo.module.excel.visitor.Visitor;

import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
class SpringBootAutoConfiguration {

    @Bean
    @ConditionalOnBean(value = Visitor.class, annotation = ValueObjectReading.class)
    ValueObjectReader valueObjectReader(List<Visitor> vs) {
        return new ValueObjectReaderImpl(vs);
    }

    @Bean
    @ConditionalOnBean(value = BatchedVisitor.class, annotation = BatchedProcessor.class)
    BatchedValueObjectReader batchedValueObjectReader(List<BatchedVisitor<?>> vs) {
        return new BatchedValueObjectReaderImpl(vs);
    }

}
