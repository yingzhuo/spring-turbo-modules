/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.rabbitmq.util;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import spring.turbo.util.Asserts;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 消息工厂
 *
 * @author 应卓
 * @see MessageSender
 * @since 1.0.15
 */
public final class MessageFactories {

    /**
     * 私有构造方法
     */
    private MessageFactories() {
    }

    public static Message create(String messageContent) {
        return create(messageContent, Long.MIN_VALUE);
    }

    public static Message create(String messageContent, long ttlInMillis) {
        return create(messageContent, ttlInMillis, Integer.MIN_VALUE);
    }

    public static Message create(String messageContent, long ttlInMillis, int priority) {
        Asserts.notNull(messageContent);

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
