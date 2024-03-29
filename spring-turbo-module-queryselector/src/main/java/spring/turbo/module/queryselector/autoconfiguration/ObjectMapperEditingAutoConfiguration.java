/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.autoconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import spring.turbo.module.queryselector.jackson2.SelectorSetJacksonModule;

/**
 * @author 应卓
 *
 * @since 2.0.1
 */
@AutoConfiguration
@ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
@ConditionalOnBean(type = "com.fasterxml.jackson.databind.ObjectMapper")
public class ObjectMapperEditingAutoConfiguration implements InitializingBean {

    private final ObjectMapper objectMapper;

    public ObjectMapperEditingAutoConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() {
        this.objectMapper.registerModules(new SelectorSetJacksonModule());
    }

}
