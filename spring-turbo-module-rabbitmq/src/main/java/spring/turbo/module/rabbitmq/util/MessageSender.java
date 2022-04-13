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
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import spring.turbo.core.SpringUtils;

/**
 * 消息发送工具
 *
 * @author 应卓
 * @see MessageFactories
 * @since 1.0.15
 */
public final class MessageSender {

    /**
     * 私有构造方法
     */
    private MessageSender() {
    }

    public static void send(String queueName, Message message) {
        send(ExchangeNames.DEFAULT, queueName, message);
    }

    public static void send(String exchange, String routingKey, Message message) {
        SpringUtils.getRequiredBean(RabbitTemplate.class)
                .send(exchange, routingKey, message);
    }

    public static void send(String exchange, String routingKey, Message message, CorrelationData correlationData) {
        SpringUtils.getRequiredBean(RabbitTemplate.class)
                .send(exchange, routingKey, message, correlationData);
    }

}
