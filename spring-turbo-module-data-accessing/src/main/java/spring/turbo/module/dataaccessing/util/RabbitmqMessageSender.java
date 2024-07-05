package spring.turbo.module.dataaccessing.util;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import spring.turbo.core.SpringUtils;
import spring.turbo.util.StringPool;

/**
 * RabbitMQ 消息发送工具
 *
 * @author 应卓
 * @see RabbitmqMessageFactories
 * @since 1.0.15
 */
public final class RabbitmqMessageSender {

    public static final String DEFAULT_EXCHANGE_NAME = StringPool.EMPTY;

    /**
     * 私有构造方法
     */
    private RabbitmqMessageSender() {
    }

    /**
     * 发送消息
     *
     * @param queueName 队列名称
     * @param message   消息
     */
    public static void send(String queueName, Message message) {
        send(DEFAULT_EXCHANGE_NAME, queueName, message);
    }

    /**
     * 发送消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息
     */
    public static void send(String exchange, String routingKey, Message message) {
        SpringUtils.getRequiredBean(RabbitTemplate.class).send(exchange, routingKey, message);
    }

    /**
     * 发送消息
     *
     * @param exchange        交换机名称
     * @param routingKey      路由键
     * @param message         消息
     * @param correlationData correlationData
     */
    public static void send(String exchange, String routingKey, Message message, CorrelationData correlationData) {
        SpringUtils.getRequiredBean(RabbitTemplate.class).send(exchange, routingKey, message, correlationData);
    }

}
