/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import spring.turbo.module.excel.ProcessPayload;

import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
class EnableValueObjectReadingConfiguration {

    @Bean
    @SuppressWarnings("rawtypes")
    ValueObjectReader valueObjectReader(List<ValueObjectListener> listenerList) {
        if (CollectionUtils.isEmpty(listenerList)) {
            return new BrokenValueObjectReader();
        } else {
            return new ValueObjectReaderImpl(listenerList);
        }
    }

    private static class BrokenValueObjectReader implements ValueObjectReader {
        public void read(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload) {
            throw new UnsupportedOperationException("ValueObjectListener not registered");
        }
    }

}
