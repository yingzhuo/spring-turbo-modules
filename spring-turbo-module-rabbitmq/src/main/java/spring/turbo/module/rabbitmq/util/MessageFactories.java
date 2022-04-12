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
 * @author 应卓
 * @since 1.0.15
 */
public final class MessageFactories {

    private MessageFactories() {
        super();
    }

    public static Message createPlainMessage(String messageContent) {
        Asserts.notNull(messageContent);
        final MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        props.setDeliveryMode(MessageDeliveryMode.PERSISTENT); // 消息要持久化必须配合队列也持久化
        return new Message(messageContent.getBytes(UTF_8), props);
    }

    public static Message createJsonMessage(String jsonContent) {
        Asserts.notNull(jsonContent);
        final MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        props.setDeliveryMode(MessageDeliveryMode.PERSISTENT); // 消息要持久化必须配合队列也持久化
        return new Message(jsonContent.getBytes(UTF_8), props);
    }

    public static Message createXmlMessage(String xmlContent) {
        Asserts.notNull(xmlContent);
        final MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_XML);
        props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);  // 消息要持久化必须配合队列也持久化
        return new Message(xmlContent.getBytes(UTF_8), props);
    }

    public static Message createBytesMessage(byte[] bytes) {
        Asserts.notNull(bytes);
        final MessageProperties props = new MessageProperties();
        props.setContentType(MessageProperties.CONTENT_TYPE_BYTES);
        props.setDeliveryMode(MessageDeliveryMode.PERSISTENT); // 消息要持久化必须配合队列也持久化
        return new Message(bytes, props);
    }

}
