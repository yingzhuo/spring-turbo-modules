/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.visitor.BatchedVisitor;
import spring.turbo.util.Asserts;

import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
class BatchedValueObjectReaderImpl implements BatchedValueObjectReader, InitializingBean {

    private final List<BatchedVisitor<?>> visitors;
//    private final Map<>

    public BatchedValueObjectReaderImpl(List<BatchedVisitor<?>> visitors) {
        this.visitors = visitors;
    }

    @Override
    public ProcessingResult read(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload) {
        Asserts.notNull(discriminator);

        String discriminatorValue = discriminator.getDiscriminatorValue();

        return null;
    }

    @Override
    public void afterPropertiesSet() {
    }

}
