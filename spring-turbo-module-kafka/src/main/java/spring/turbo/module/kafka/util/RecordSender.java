/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.kafka.util;

import org.springframework.kafka.core.KafkaTemplate;
import spring.turbo.core.SpringUtils;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.15
 */
public final class RecordSender {

    /**
     * 私有构造方法
     */
    private RecordSender() {
        super();
    }

    public static void send(String topic, String record) {
        Asserts.notNull(record);
        final KafkaTemplate<String, String> template = getTemplate();
        template.send(topic, record);
    }

    public static void send(String topic, String key, String record) {
        Asserts.notNull(record);
        final KafkaTemplate<String, String> template = getTemplate();
        template.send(topic, key, record);
    }

    public static void send(String topic, int partition, String key, String record) {
        Asserts.isTrue(partition >= 0);
        Asserts.notNull(record);
        final KafkaTemplate<String, String> template = getTemplate();
        template.send(topic, partition, key, record);
    }

    private static KafkaTemplate<String, String> getTemplate() {
        return SpringUtils.getRequiredBean(KafkaTemplate.class);
    }

}
