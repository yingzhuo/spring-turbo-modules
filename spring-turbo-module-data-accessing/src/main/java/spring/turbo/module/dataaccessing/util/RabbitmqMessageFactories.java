package spring.turbo.module.dataaccessing.util;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * RabbitMQ 消息生成工具
 *
 * @author 应卓
 * @see RabbitmqMessageSender
 * @since 1.0.15
 */
public final class RabbitmqMessageFactories {

    /**
     * 私有构造方法
     */
    private RabbitmqMessageFactories() {
    }

    /**
     * 创建消息实体
     *
     * @param messageContent 消息内容
     * @return 消息实例
     */
    public static Message create(String messageContent) {
        return create(messageContent, Long.MIN_VALUE);
    }

    /**
     * 创建消息实体
     *
     * @param messageContent 消息内容
     * @param ttlInMillis    生命期 (毫秒)
     * @return 消息实例
     */
    public static Message create(String messageContent, long ttlInMillis) {
        return create(messageContent, ttlInMillis, Integer.MIN_VALUE);
    }

    /**
     * 创建消息实体
     *
     * @param messageContent 消息内容
     * @param ttlInMillis    生命期 (毫秒)
     * @param priority       优先级
     * @return 消息实例
     */
    public static Message create(String messageContent, long ttlInMillis, int priority) {
        final MessageProperties props = new MessageProperties();

        // 内容类型: byte[]
        props.setContentType(MessageProperties.CONTENT_TYPE_BYTES);

        // 持久化
        props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

        // 对每条消息设置TTL
        if (ttlInMillis > 0) {
            props.setExpiration(Long.toString(ttlInMillis));
        }

        // 优先权
        if (priority >= 0) {
            props.setPriority(priority);
        }

        return new Message(messageContent.getBytes(UTF_8), props);
    }

}
