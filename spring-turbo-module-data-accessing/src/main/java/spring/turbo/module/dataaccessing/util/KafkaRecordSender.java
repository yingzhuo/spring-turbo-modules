package spring.turbo.module.dataaccessing.util;

import org.springframework.kafka.core.KafkaTemplate;
import spring.turbo.core.SpringUtils;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.0.15
 */
public final class KafkaRecordSender {

    /**
     * 私有构造方法
     */
    private KafkaRecordSender() {
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

    @SuppressWarnings("unchecked")
    private static KafkaTemplate<String, String> getTemplate() {
        return SpringUtils.getRequiredBean(KafkaTemplate.class);
    }

}
